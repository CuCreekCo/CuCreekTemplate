package com.cucreek.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

/**
 * Represents the user's role in the app
 *
 * @author jljdavidson
 */
@Entity
public class UserRole {

   @ManyToOne
   private UserRoleCode userRoleCode;

   @ManyToOne
   private UserAccount userAccount;

   @Version
   private Integer version;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE)
   private Long userRoleId;

   public UserAccount getUserAccount() {
      return userAccount;
   }

   public void setUserAccount(UserAccount userAccount) {
      this.userAccount = userAccount;
   }

   public Integer getVersion() {
      return version;
   }

   public void setVersion(Integer version) {
      this.version = version;
   }

   public Long getUserRoleId() {
      return userRoleId;
   }

   public void setUserRoleId(Long id) {
      this.userRoleId = id;
   }

   public UserRoleCode getUserRoleCode() {
      return userRoleCode;
   }

   public void setUserRoleCode(UserRoleCode userRoleCode) {
      this.userRoleCode = userRoleCode;
   }
}
