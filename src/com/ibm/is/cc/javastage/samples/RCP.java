//***************************************************************************
// (c) Copyright IBM Corp. 2012 All rights reserved.
// 
// The following sample of source code ("RCP") is owned by International 
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

import com.ibm.is.cc.javastage.api.ColumnMetadata;
import com.ibm.is.cc.javastage.api.Configuration;
import com.ibm.is.cc.javastage.api.InputLink;
import com.ibm.is.cc.javastage.api.InputRecord;
import com.ibm.is.cc.javastage.api.Link;
import com.ibm.is.cc.javastage.api.OutputLink;
import com.ibm.is.cc.javastage.api.OutputRecord;
import com.ibm.is.cc.javastage.api.Processor;
import com.ibm.is.cc.javastage.api.Capabilities;
import java.util.Properties;
import java.util.List;

/**
 * <code>RCP</code>  is a sample program for column-based transformer stage using
 * RCP(Runtime Column Propagation). 
 * <p>
 * It reads a row having empno(BigInt), firstname(VarChar), lastname(VarChar), hireDate(Date),
 * edLevel(Integer), salary(Double), bonus(Double) and lastUpdate(Timestamp[microseconds])columns, 
 * converts the firstname and lastname value to uppercase.
 * There is only "firstname" and "lastname" column defined in the output link (Runtime column propagation check box is checked). 
 * <p>
 * If RCP is on, all columns in input link will be propagated to output link. Columns other than "firstname" and
 * and "lastname" is sent to output link without any changes. <br>
 * If RCP is off, only "firstname" and "lastname" will be sent to output link.
 *
 * <p>Here is a list of DataStage jobs using this class:</p>
 * <ul>
 * <li><code>RCP</code>
 * </ul>
 *
 * @version 1.0
 */
public class RCP extends Processor
{
   private InputLink m_inputLink;
   private OutputLink m_outputLink;

   public RCP()
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
    * <li> Maximum number of reject links is "0"
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
      // Set maximum number of reject links to 0
      capabilities.setMaximumRejectLinkCount(0);
      // Set is Wave Generator to false
      capabilities.setIsWaveGenerator(false);
      return capabilities;
   }

   /**
    * Specifies the current configuration (number and types of links).
    *
    * @param configuration The current configuration(number and types of links, environment)
    * @param isRuntime <code>true</code> if runtime, otherwise <code>false</code>.
    * @return <code>false</code> if found problems with the configurations. Otherwise, returns <code>true</code>.
    */
   public boolean validateConfiguration(Configuration configuration, 
                                        boolean       isRuntime) throws Exception
   {

      m_inputLink = configuration.getInputLink(0);
      m_outputLink = configuration.getOutputLink(0);

      return true;
   }

   /**
    * Read records from the input and send alternate rows to the stream output link.
    * <p>
    * It reads a row having name and cost columns, converts the name value to uppercase,
    * accumulates the cost value, and writes a row into output link.
    * <p>
    * 
    * @exception Exception if error occurred during processing records.
    */
   public void process() throws Exception
   {
      do
      {
         InputRecord inputRecord = m_inputLink.readRecord();
         if (inputRecord == null)
         {
             // No more input
             break;
         }

         // Create an output record
         OutputRecord outputRecord = m_outputLink.getOutputRecord();

         String firstname = (String)inputRecord.getValue("firstname");
         outputRecord.setValue("firstname", firstname.toUpperCase());
               
         String lastname = (String)inputRecord.getValue("lastname");
         outputRecord.setValue("lastname", lastname.toUpperCase());

         // This performs mapping from my object to the same-named columns.
         m_outputLink.writeRecord(outputRecord);

     } while (true);
  }
}
