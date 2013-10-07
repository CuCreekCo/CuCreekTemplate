package com.cucreek.persistence.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple user account
 *
 * @author jljdavidson
 */
@Entity
public class UserAccount {

   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "userAccount")
   private Set<UserRole> userRoles = new HashSet<UserRole>();

   @NotNull
   @Column(unique = true)
   private String username;
   @NotNull
   @Column(unique = true)
   private String email;
   @NotNull
   private String password;
   @NotNull
   private Boolean isSuspended;
   @Id
   @GeneratedValue(strategy = GenerationType.TABLE)
   private Long userAccountId;
   @Version
   private Integer version;

   public String toString() {
      return ReflectionToStringBuilder.
                                            toStringExclude(this,
                                                  new String[]{"password", "email"});
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Boolean getSuspended() {
      return isSuspended;
   }

   public void setSuspended(Boolean suspended) {
      isSuspended = suspended;
   }

   public Long getUserAccountId() {
      return userAccountId;
   }

   public void setUserAccountId(Long id) {
      this.userAccountId = id;
   }

   public Integer getVersion() {
      return version;
   }

   public void setVersion(Integer version) {
      this.version = version;
   }

   public Set<UserRole> getUserRoles() {
      return userRoles;
   }

   public void setUserRoles(Set<UserRole> userRoles) {
      this.userRoles = userRoles;
   }
}


