//***************************************************************************
// (c) Copyright IBM Corp. 2012 All rights reserved.
// 
// The following sample of source code ("IntValueGenerator") is owned by International 
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

import com.ibm.is.cc.javastage.api.Capabilities;
import com.ibm.is.cc.javastage.api.Configuration;
import com.ibm.is.cc.javastage.api.OutputLink;
import com.ibm.is.cc.javastage.api.OutputRecord;
import com.ibm.is.cc.javastage.api.Processor;
import com.ibm.is.cc.javastage.api.ColumnMetadata;
import com.ibm.is.cc.javastage.api.Logger;
import com.ibm.is.cc.javastage.api.PropertyDefinition;

import java.util.List;
import java.util.Properties;
import java.util.ArrayList;

/**
 * <code>IntValueGenerator</code> is a sample program for column-based source stage. 
 * The job has 1 Integer column.
 * It writes rows to an output link. The number of rows to generate is specified as 
 * the user's custom stage property "NumOfRecords" value in the Java Integration stage and is fetched using 
 * the <code>Configuration.getUserProperties()</code> method. Default value is 10.
 * <p>
 * End-of-wave marker is written based on the user's custom output link property "WaveCount".
 * Based on the specified value, end-of-wave marker is written after specified number
 * of records. Default value is 5.
 * <p>
 * Generated value for Integer columns are the incremented integer value starts from 0.
 * If the job is running on multi-node environment, generation is distributed across
 * player nodes in round-robin method.
 * For example, if job is running on 3 nodes and "NumOfRecords" is set to 10, records
 * are generated as follows:
 * <p>
 * node 0 - 0, 3, 6, 9
 * <p>
 * node 1 - 1, 4, 7
 * <p>
 * node 2 - 2, 5, 8
 * <p>
 *
 * <p>Here is a list of DataStage jobs using this class:</p>
 * <ul>
 * <li><code>IntValueGenerator</code>
 * </ul>
 *
 * @version 1.0
 */
public class IntValueGenerator extends Processor
{
   private OutputLink m_outputLink;
   private int m_numOfRecords = 10;
   private int m_waveCount = 5;
   private List<String> m_errors = new ArrayList<String>();
   private int m_nodeID = -1;
   private int m_numOfNodes = -1;

   public IntValueGenerator()
   {
     super();
   }

   /**
    * Overrides default values of capabilities.
    * It will check whether this program can be run with the current job design.
    *
    * <li> Maximum number of input links is "0"
    * <li> Minimum number of output stream links is "1"
    * <li> Maximum number of output stream links is "1"
    * <li> Maximum number of reject links is "0"
    * <li> Is Wave Generator : true
    *
    * @return Capabilities
    */
   public Capabilities getCapabilities()
   {
      Capabilities capabilities = new Capabilities();
      // Set maximum number of input links to 1
      capabilities.setMaximumInputLinkCount(0);
      // Set minimum number of output stream links to 1
      capabilities.setMinimumOutputStreamLinkCount(1);
      // Set maximum number of output stream links to 1
      capabilities.setMaximumOutputStreamLinkCount(1);
      // Set maximum number of reject links to 0
      capabilities.setMaximumRejectLinkCount(0);
      // Set is Wave Generator to true
      capabilities.setIsWaveGenerator(true);
      return capabilities;
   }

   /**
    * Specifies custom properties using PropertyDefinition object.
    * The value can be set from Stage GUI "Custom Property Editor" by
    * clicking "Configuration" button.
    * <p>
    * "RecordNum" custom property is defined as STAGE property.
    * <p>
    * "WaveCount" custom property is defined as OUTPUT_LINK_ONLY property.
    * @return List property list
    */

   public List<PropertyDefinition> getUserPropertyDefinitions()
   {
      List<PropertyDefinition> propList = new ArrayList<PropertyDefinition>();

      propList.add(new PropertyDefinition("NumOfRecords",
                                          "10", 
                                          "Number of Records", 
                                          "Specifies the number of record to be generated.",
                                          PropertyDefinition.Scope.STAGE)); 

      propList.add(new PropertyDefinition("WaveCount",
                                          "5", 
                                          "Wave Count", 
                                          "Specifies the number of record to be processed before writing end-of-wave marker.",
                                          PropertyDefinition.Scope.OUTPUT_LINK_ONLY)); 
      return propList;
   }

   /**
    * Specifies the current configuration (number and types of links), and the values
    * for the user properties. If there are problems with the configuration or user properties,
    * and return <code>false</code> and <code>getConfigurationErrors()</code> will be invoked
    * to show the error messages.
    * <p>
    * Fetches the number of rows to generate from the User's Properties "RecordNum" value
    * by calling <code>Configuration.getUserProperties()</code> method.
    *
    * @param configuration The current configuration(number and types of links, environment)
    * @param isRuntime <code>true</code> if runtime, otherwise <code>false</code>.
    * @return <code>false</code> if found problems with the configurations. Otherwise, returns <code>true</code>.
    */
   public boolean validateConfiguration(Configuration configuration, 
                                        boolean       isRuntime) throws Exception
   {
      // Specify current link configurations.
      m_outputLink = configuration.getOutputLink(0);

      Properties userStageProperties = configuration.getUserProperties();
      String propValue;
      // Fetch the value of "NumOfRecords" user property.
      // If it is not specified, use default value 10.
      // The minimum number of NumOfRecords is 0.
      // The maximum number of NumOfRecords is 100.
      // If specified value is out of range, return error.
      propValue = userStageProperties.getProperty("NumOfRecords");
      if (propValue != null)
      {
         m_numOfRecords = Integer.valueOf(propValue);
      }
      if (m_numOfRecords < 0 || m_numOfRecords > 100)
      {
         m_errors.add("Please set the NumOfRecords property value between 1 to 100.");
      }

      // Fetch the value of "WaveCount" user property.
      // If it is not specified, use default value 5.
      // The minimum number of WaveCount is 0.
      // The maximum number of WaveCount is 100.
      // If specified value is out of range, return error.
      Properties userLinkProperties = m_outputLink.getUserProperties();
      propValue = userLinkProperties.getProperty("WaveCount");
      if (propValue != null)
      {
         m_waveCount = Integer.valueOf(propValue);
      }
      if (m_waveCount < 0 || m_waveCount > 100)
      {
         m_errors.add("Please set the waveCount property value between 1 to 100.");
      }
      
      // If the job is running on multi-node configuration,
      // we want to process user program in parallel.
      // If we are to generate 100 records in this user program,
      // and if there are 2 nodes defined, 2 processes will generate
      // 100 records each, and the job will generate 200 recrods in total.
      // In order to process the program in parallel, we need to fetch
      // the current node id were the player process is running on, 
      // and the total number of nodes where Java Integration stage will be running on.
      // Node ID starts from 0.
      m_nodeID = configuration.getNodeNumber();
      Logger.information("current node id = " + m_nodeID);
      
      m_numOfNodes = configuration.getNodeCount();
      Logger.information("number of nodes = " + m_numOfNodes);

      List<ColumnMetadata> columns = m_outputLink.getColumnMetadata();
      
      // This program only supports 1 Integer column.
      // Return error if job has more than 1 column.
      if (columns.size() > 1)
      {
         m_errors.add("More than one columns are defined.");
      }
      else
      {
         // There should be only 1 column. Get column metadata information.
         ColumnMetadata column = columns.get(0);
         if (column.getSQLType() != ColumnMetadata.SQL_TYPE_INTEGER)
         {
            m_errors.add("Single Integer column is supported.");
         }
      }
      
      if (m_errors.size() > 0)
      {
         return false;
      }
      
      return true;
   }
   
   /**
    * Returns an ordered list of textual errors describing the issue with
    * the configuration. This method will be called if {@link #validateConfiguration} returned <code>false</code>.
    *
    * @return An ordered list of textual errors describing the issue with the configuration.
    */
   public List<String> getConfigurationErrors()
   {
      return m_errors;
   }

   /**
    * Generate integer value and write into stream output link.
    * <p>
    * Number of records generated will be the value of "NumOfRecords" custom property if
    * specified. Otherwise, generates 10 records.
    * End of wave marker is written into output based on the "WaveCount" custom property
    * if specified. Otherwise, write wave marker for each 5 records.
    * <p>
    * Based on the number of nodes and the node ID where this player is running on,
    * generation of integer value is distrubuted across players.
    * @exception Exception if error occurred during processing records.
    */ 
   public void process() throws Exception
   {
      OutputRecord outputRecord = m_outputLink.getOutputRecord();
      
      Logger.information("NumOfRecords is set to: " + m_numOfRecords);
      Logger.information("WaveCount is set to: " + m_waveCount);
      
      int endValue = m_numOfRecords;
      int value = m_nodeID;
      int numGenerated = 0;
      
      // Generate the value based on the number of nodes defined for this job,
      // and the current node ID where this player process is running on.
      while (value < endValue)
      {
         outputRecord.setValue(0, (long)value);
         value += m_numOfNodes;

         numGenerated++;
         m_outputLink.writeRecord(outputRecord);

         // If number of generated records reached the wave count,
         // write end-of-wave marker.
         if (numGenerated == m_waveCount)
         {
            // Write end-of-wave marker
            m_outputLink.writeWaveMarker();
            numGenerated = 0;
         }
      }
   }
}
