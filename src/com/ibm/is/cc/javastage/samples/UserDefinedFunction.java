//***************************************************************************
// (c) Copyright IBM Corp. 2012 All rights reserved.
// 
// The following sample of source code ("UserDefinedFunction") is owned by International 
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

import com.ibm.is.cc.javastage.samples.InputBean;
import com.ibm.is.cc.javastage.samples.UDFOutputBean;


/**
 * <code>UserDefinedFunction</code> class contains a sample method
 * being invoked as User Defined Functions from Java Integration stages.
 *
 * <p>Here is a list of DataStage jobs using this method:</p>
 *
 * <ul>
 * <li><code>UserDefinedFunction</code>
 * </ul>
 *
 * @version 1.0
 *
 */
public class UserDefinedFunction
{

   /**
    * Passes primitive type double and a bean as UDF arguments and returns a bean. 
    *
    * @param commission commission
    * @param input {@link InputBean} object.
    * @return output {@link UDFOutputBean} object.
    */
   public UDFOutputBean AnnualIncome(double commission, InputBean input)
   {
      UDFOutputBean output = new UDFOutputBean();

      output.setEmpno(input.getEmpno());
      output.setFirstName(input.getFirstName().toUpperCase());
      output.setLastName(input.getLastName().toUpperCase());

      double total = commission + input.getSalary().doubleValue() + input.getBonus();
      output.setIncome(total);
      
      return output;
   }

}
