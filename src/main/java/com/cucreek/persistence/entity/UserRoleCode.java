package com.cucreek.persistence.entity;

import com.cucreek.common.CodeTableEntityAnnotation;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the user role codes
 *
 * @author jljdavidson
 */
@Entity
@CodeTableEntityAnnotation(description = "roleDescription", code = "roleCode", endDate = "endDate",
   effectiveDate = "effectiveDate")
public class UserRoleCode {

   @Id
   @Column
   private String roleCode;

   @Column
   private String roleDescription;

   @Column
   @Temporal(TemporalType.TIMESTAMP)
   @DateTimeFormat(style = "MM")
   private Date effectiveDate;

   @Column
   @Temporal(TemporalType.TIMESTAMP)
   @DateTimeFormat(style = "MM")
   private Date endDate;

   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "userRoleCode")
   private Set<UserRole> userRoles = new HashSet<UserRole>();

   @Version
   private Integer version;

   public String getRoleCode() {
      return roleCode;
   }

   public void setRoleCode(String roleCode) {
      this.roleCode = roleCode;
   }

   public String getRoleDescription() {
      return roleDescription;
   }

   public void setRoleDescription(String roleDescription) {
      this.roleDescription = roleDescription;
   }

   public Date getEffectiveDate() {
      return effectiveDate;
   }

   public void setEffectiveDate(Date effectiveDate) {
      this.effectiveDate = effectiveDate;
   }

   public Date getEndDate() {
      return endDate;
   }

   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   public Set<UserRole> getUserRoles() {
      return userRoles;
   }

   public void setUserRoles(Set<UserRole> userRoles) {
      this.userRoles = userRoles;
   }

   public Integer getVersion() {
      return version;
   }

   public void setVersion(Integer version) {
      this.version = version;
   }
}
