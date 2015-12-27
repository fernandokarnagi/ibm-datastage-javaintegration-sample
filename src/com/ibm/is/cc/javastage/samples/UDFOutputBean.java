//***************************************************************************
// (c) Copyright IBM Corp. 2012 All rights reserved.
// 
// The following sample of source code ("UDFOutputBean") is owned by International 
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

/**
 * <code>UDFOutputBean</code> is a Java bean for an output link of User-defined Function
 *
 * @version 1.0
 */
public class UDFOutputBean
{
   private long      m_empno;
   private String    m_firstname;
   private String    m_lastname;
   private double    m_income;
   
   /**
    * Fetches the value of the empno field.
    *
    * @return long value of empno field
    */
   public long getEmpno()
   {
      return m_empno;
   }
   
   /**
    * Set the value of the empno field.
    *
    * @param empno value of the empno field.
    */
   public void setEmpno(long empno)
   {
      m_empno = empno;
   }
   
   /**
    * Fetches the value of the firstname field.
    *
    * @return String value of firstname field
    */
   public String getFirstName()
   {
      return m_firstname;
   }
   
   /**
    * Set the value of the firstname field.
    *
    * @param firstname value of the firstname field.
    */
   public void setFirstName(String firstname)
   {
      m_firstname = firstname;
   }
    
   /**
    * Fetches the value of the lastname field.
    *
    * @return String value of lastname field
    */
   public String getLastName()
   {
      return m_lastname;
   }
   
   /**
    * Set the value of the lastname field.
    *
    * @param lastname value of the lastname field.
    */
   public void setLastName(String lastname)
   {
      m_lastname = lastname;
   }

   /**
    * Fetches the value of the income field.
    *
    * @return double value of income field
    */
   public double getIncome()
   {
      return m_income;
   }
   
   /**
    * Set the value of the total field.
    *
    * @param income value of the total field.
    */
   public void setIncome(double income)
   {
      m_income = income;
   }
}
