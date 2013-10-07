/**
 *
 */
package com.cucreek.persistence.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * This data transfer object class provides a code table abstraction for all code tables.
 *
 * @author jljdavidson
 */


public class CodeTableDTO extends BasePersistenceDTO {
   private static final long serialVersionUID = -3906413251768692915L;
   private String codeValue;
   private String oldCodeValue;
   private String description;
   private String codeValuePlusDescription;
   @NotNull
   private Date effectiveDate;
   private Date endDate;

   public CodeTableDTO() {
      super();
   }

   public CodeTableDTO(String codeValue, String description) {
      this(codeValue, description, null, null);
   }

   public CodeTableDTO(Long codeValue, String description) {
      this(codeValue.toString(), description, null, null);
   }

   public CodeTableDTO(Long codeValue, String description, Date effectiveDate) {
      this(codeValue.toString(), description, effectiveDate, null);
   }

   public CodeTableDTO(Long codeValue, String description, Date effectiveDate, Date endDate) {
      this(codeValue.toString(), description, effectiveDate, endDate);
   }

   /**
    * Construction object from code value, description, effective date, and end date
    *
    * @param codeValue     string
    * @param description   string
    * @param effectiveDate date
    * @param endDate       optional end date
    */
   public CodeTableDTO(String codeValue, String description, Date effectiveDate, Date endDate) {
      this.codeValue = codeValue;
      this.oldCodeValue = codeValue;
      this.description = description;
      this.effectiveDate = effectiveDate;
      this.endDate = endDate;
      this.codeValuePlusDescription = codeValue + " - " + description;
   }

   @Override
   public boolean equals(Object thatObject) {
      if (thatObject instanceof CodeTableDTO) {
         CodeTableDTO that = (CodeTableDTO) thatObject;

         return StringUtils.equals(that.getCodeValue(), this.getCodeValue());
      }
      return false;
   }

   @Override
   public int hashCode() {
      return new HashCodeBuilder(7, 737).
                                              append(codeValue).
                                              append(description).
                                              toHashCode();
   }

   public Date getEndDate() {
      return this.endDate;
   }

   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   public Date getEffectiveDate() {
      return this.effectiveDate;
   }

   public void setEffectiveDate(Date effectiveDate) {
      this.effectiveDate = effectiveDate;
   }

   public String getCodeValuePlusDescription() {
      return this.codeValuePlusDescription;
   }

   public void setCodeValuePlusDescription(String codeValuePlusDescription) {
      this.codeValuePlusDescription = codeValuePlusDescription;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getOldCodeValue() {
      return this.oldCodeValue;
   }

   public void setOldCodeValue(String oldCodeValue) {
      this.oldCodeValue = oldCodeValue;
   }

   public String getCodeValue() {
      return this.codeValue;
   }

   public void setCodeValue(String codeValue) {
      this.codeValue = codeValue;
   }

   public String toString() {
      return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
   }
}
