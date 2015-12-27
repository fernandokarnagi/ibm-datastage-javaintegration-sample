//***************************************************************************
// (c) Copyright IBM Corp. 2012 All rights reserved.
// 
// The following sample of source code ("JavaPackTransformer") is owned by International 
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

import com.ascentialsoftware.jds.Row;
import com.ascentialsoftware.jds.Stage;

/**
 * <code>JavaPackTransformer</code> is a sample program for Java Pack Transformer stage.
 * This program can be executed from Java Pack Transformer Plugin Stage and Java Integration stage.
 * It reads a row having empno(BigInt), firstname(VarChar), lastname(VarChar), hireDate(Date),
 * edLevel(Integer), salary(Double), bonus(Double) and lastUpdate(Time[microseconds])columns,
 * converts the firstname and lastname value to uppercase. 
 * If firstname column of the input row contains the character '*', the row is rejected. 
 *
 * <p>Here is a list of DataStage jobs using this class:</p>
 * <ul>
 * <li><code>JavaPackTransformer</code>
 * </ul>
 *
 * @version 1.0
 */
public class JavaPackTransformer extends Stage
{
   /**
    * Process rows from the input link and convert all its columns to upper case,
    * and write the result int output link. If one column of the input row
    * contains the character '*', the row is rejected.
    *
    * @return {@link #OUTPUT_STATUS_NOT_READY}, {@link #OUTPUT_STATUS_READY} or
    *         {@link #OUTPUT_STATUS_END_OF_DATA}
    */

   public int process()
   {
      // Read a row, convert all its columns to upper case,
      // and write the result. If one column of the input row
      // contains the character '*', the row is rejected.
      Row inputRow = readRow();
      if (inputRow == null) 
      {
         return OUTPUT_STATUS_END_OF_DATA;
      }
      boolean reject = false;
      int columnCount = inputRow.getColumnCount();
      Row outputRow = createOutputRow();
      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) 
      {
         
         String value = inputRow.getValueAsString(columnIndex);
         if ((value == null) || (value.indexOf('*') >= 0)) 
         {
            reject = true;
            outputRow.setValueAsString(columnIndex, value);
         } 
         else 
         {
            outputRow.setValueAsString(columnIndex, value.toUpperCase());
         }
      }
      if (reject) 
      {
         rejectRow(outputRow);
      } 
      else 
      {
         writeRow(outputRow);
      }
      return OUTPUT_STATUS_READY;
   }
}
