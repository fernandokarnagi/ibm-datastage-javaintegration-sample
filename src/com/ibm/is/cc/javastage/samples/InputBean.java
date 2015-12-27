//***************************************************************************
// (c) Copyright IBM Corp. 2012 All rights reserved.
// 
// The following sample of source code ("InputBean") is owned by International 
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

import java.sql.Date;
import java.sql.Time;

/**
 * <code>InputBean</code> is a Java bean for an input link of sample programs
 *
 * @version 1.0
 */
public class InputBean
{
   private long      m_empno;
   private String    m_firstname;
   private String    m_lastname;
   private Date      m_hiredate;
   private int       m_edlevel;
   private Double    m_salary;
   private double    m_bonus;
   private Time      m_lastupdate;
   
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
    * Fetches the value of the hiredate field.
    *
    * @return Date value of hiredate field
    */
   public Date getHireDate()
   {
      return m_hiredate;
   }
  
   /**
    * Set the value of the hiredate field.
    *
    * @param hiredate value of the hiredate field.
    */
   public void setHireDate(Date hiredate)
   {
      m_hiredate = hiredate;
   }

   /**
    * Fetches the value of the edlevel field.
    *
    * @return int value of edlevel field
    */
   public int getEdLevel()
   {
      return m_edlevel;
   }
  
   /**
    * Set the value of the edlevel field.
    *
    * @param edlevel value of the edlevel field.
    */
   public void setEdLevel(int edlevel)
   {
      m_edlevel = edlevel;
   }

   /**
    * Fetches the value of the salary field.
    *
    * @return Double value of salary field
    */
   public Double getSalary()
   {
      return m_salary;
   }
   
   /**
    * Set the value of the salary field.
    *
    * @param salary value of the salary field.
    */
   public void setSalary(Double salary)
   {
      m_salary = salary;
   }

   /**
    * Fetches the value of the bonus field.
    *
    * @return double value of bonus field
    */
   public double getBonus()
   {
      return m_bonus;
   }
   
   /**
    * Set the value of the bonus field.
    *
    * @param bonus value of the bonus field.
    */
   public void setBonus(double bonus)
   {
      m_bonus = bonus;
   }
   
   /**
    * Fetches the value of the lastupdate field.
    *
    * @return Time value of lastupdate field
    */
   public Time getLastUpdate()
   {
      return m_lastupdate;
   }
   
   /**
    * Set the value of the lastupdate field.
    *
    * @param lastupdate value of the lastupdate field.
    */
   public void setLastUpdate(Time lastupdate)
   {
      m_lastupdate = lastupdate;
   }
   
}
