//***************************************************************************
// (c) Copyright IBM Corp. 2012 All rights reserved.
// 
// The following sample of source code ("JavaBeansTransformer") is owned by International 
// Business Machines Corporation or one of its subsidiaries ("IBM") and is 
// copyrighted and licensed, not sold. You may use, copy, modify, and 
// distribute the Sample in any form without payment to IBM, for the purpose of 
// assisting you in the development of your applications.
// 
// The Sample code is provided to you on an "AS IS" basis, without warranty of 
// any kind. IBM HEREBY EXPRESSLY DISCLAIMS ALL WARRANTIES, EITHER EXPRESS OR 
// IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. Some jurisdictions do 
// not allow for the exclusion or limitation of implied warranties, so the above 
// limitations or exclusions may not apply to you. IBM shall not be liable for 
// any damages you suffer as a result of using, copying, modifying or 
// distributing the Sample, even if IBM has been advised of the possibility of 
// such damages.
//***************************************************************************
package com.ibm.is.cc.javastage.samples;

import com.ibm.is.cc.javastage.api.Configuration;
import com.ibm.is.cc.javastage.api.InputLink;
import com.ibm.is.cc.javastage.api.InputRecord;
import com.ibm.is.cc.javastage.api.Link;
import com.ibm.is.cc.javastage.api.OutputLink;
import com.ibm.is.cc.javastage.api.OutputRecord;
import com.ibm.is.cc.javastage.api.RejectRecord;
import com.ibm.is.cc.javastage.api.Processor;
import com.ibm.is.cc.javastage.api.Capabilities;
import com.ibm.is.cc.javastage.samples.InputBean;
import com.ibm.is.cc.javastage.samples.OutputBean;

/**
 * <code>JavaBeansTransformer</code> is a sample program for bean-based transformer stage. 
 * It reads a row having empno(BigInt), firstname(VarChar), lastname(VarChar), hireDate(Date),
 * edLevel(Integer), salary(Double), bonus(Double) and lastUpdate(Time)columns, 
 * converts the firstname and lastname value to uppercase,
 * If firstname column of the input row contains the character '*', the row is rejected.
 * In rejected records,"ERRORTEXT" and "ERRORCODE" fields are added to show the rejected reason.
 *
 * <p>Here is a list of DataStage jobs using this class:</p>
 * <ul>
 * <li><code>JavaBeansTransformer</code>
 * </ul>
 *
 * @version 1.0
 */
public class JavaBeansTransformer extends Processor
{
   private InputLink m_inputLink;
   private OutputLink m_outputLink;
   private OutputLink m_rejectLink;

   public JavaBeansTransformer()
   {
      super();
   }

   /**
    * Overrides default values of capabilities.
    * It will check whether this program can be run with the current job design.
    *
    * <li> Minimum number of input links is "1"
    * <li> Maximum number of input links is "1"
    * <li> Minimum number of output stream links is "1"
    * <li> Maximum number of output stream links is "1"
    * <li> Maximum number of reject links is "1"
    * <li> Is Wave Generator : false
    *
    * @return Capabilities
    */
   public Capabilities getCapabilities()
   {
      Capabilities capabilities = new Capabilities();
      // Set minimum number of input links to 1
      capabilities.setMinimumInputLinkCount(1);
      // Set maximum number of input links to 1
      capabilities.setMaximumInputLinkCount(1);
      // Set minimum number of output stream links to 1
      capabilities.setMinimumOutputStreamLinkCount(1);
      // Set maximum number of output stream links to 1
      capabilities.setMaximumOutputStreamLinkCount(1);
      // Set maximum number of reject links to 1
      capabilities.setMaximumRejectLinkCount(1);
      // Set is Wave Generator to false
      capabilities.setIsWaveGenerator(false);
      return capabilities;
   }

   /**
    * Specifies the current configuration (number and types of links)
    *
    * @param configuration The current configuration(number and types of links, environment)
    * @param isRuntime <code>true</code> if runtime, otherwise <code>false</code>.
    * @return <code>false</code> if found problems with the configurations. Otherwise, returns <code>true</code>.
    */
   public boolean validateConfiguration(Configuration configuration, 
                                        boolean       isRuntime) throws Exception
   {
      // Specify current link configurations.
      m_inputLink = configuration.getInputLink(0);
      m_outputLink = configuration.getOutputLink(0);
      
      if (configuration.getRejectLinkCount() == 1)
      {
         m_rejectLink = m_inputLink.getAssociatedRejectLink();
      }
      
      return true;
   }

   /**
    * Read records from the input and send alternate rows to the reject link, or 
    * stream output link.
    *
    * If the value of the firstname column contains "*" character, mark reject flag and 
    * send the record to reject link. In rejected record, "ERRORTEXT" and "ERRORCODE"
    * fields are added to show the rejected reason.
    *
    * @exception Exception if error occurred during processing records.
    */ 
   public void process() throws Exception
   {
      OutputRecord outputRecord = m_outputLink.getOutputRecord();

      // Loop until there is no more input data
      do
      {
         InputRecord record = m_inputLink.readRecord();
         if (record == null)
         {
            // End of data
            break;
         }

         // Get the object from the input row.
         InputBean inputBean = (InputBean) record.getObject();

         // Get the value from name column of the input link.
         // If the value contains "*" character, mark reject flag and send the record
         // to reject link in later proceessing.
         boolean fReject = false;
         String name = inputBean.getFirstName();
         if ((name == null) || (name.indexOf('*') >= 0)) 
         {
            fReject = true;
         }

         if (!fReject)
         {
            // Send record to output
            OutputBean outputBean = new OutputBean();
            outputBean.setEmpno(inputBean.getEmpno());
            outputBean.setFirstName(inputBean.getFirstName().toUpperCase());
            outputBean.setLastName(inputBean.getLastName().toUpperCase());
            outputBean.setHireDate(inputBean.getHireDate());
            outputBean.setEdLevel(inputBean.getEdLevel());
            outputBean.setSalary(inputBean.getSalary());
            outputBean.setBonus(inputBean.getBonus());
            outputBean.setLastUpdate(inputBean.getLastUpdate());
            outputRecord.putObject(outputBean);
            m_outputLink.writeRecord(outputRecord);
         }
         else if (m_rejectLink != null)
         {
            // Reject record.  This transfers the row to the reject link.
            // The same kind of forwarding is also possible for regular stream
            // links.
            RejectRecord rejectRecord = m_rejectLink.getRejectRecord(record);

            // Reject record can contain additional columns "ERRORTEXT" and "ERRORCODE".
            // The field will be shown as columns in rejected output records.
            rejectRecord.setErrorText("Name field contains *");
            rejectRecord.setErrorCode(123);
            m_rejectLink.writeRecord(rejectRecord);

         }
      } 
      while (true);
   }

   /**
    * Returns the bean class corresponding to <code>inputLink</code>.
    *
    * @param inputLink {@link Link}
    * @return A {@link Class} of bean class.
    */
   public Class<InputBean> getBeanForInput(Link inputLink)
   {
      return InputBean.class;
   }

   /**
    * Returns the bean class corresponding to <code>outputLink</code>.
    *
    * @param outputLink {@link Link}
    * @return A {@link Class} of bean class.
    */
   public Class<OutputBean> getBeanForOutput(Link outputLink)
   {
      return OutputBean.class;
   }
}
