package com.cucreek.persistence.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;

/**
 * JPA Entity for DFS_REGIONS
 *
 * @author jljdavidson
 */
@Entity
public class HelloEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE)
   private Long helloId;

   @Version
   private Integer version;

   @Column(nullable = false)
   private String saySomething;

   @Column
   private String helloTypeCode;

   @Column
   @Temporal(TemporalType.TIMESTAMP)
   @DateTimeFormat(style = "MM")
   private Date helloDate;


   public Long getHelloId() {
      return helloId;
   }

   public void setHelloId(Long helloId) {
      this.helloId = helloId;
   }

   public Integer getVersion() {
      return version;
   }

   public void setVersion(Integer version) {
      this.version = version;
   }

   public String getSaySomething() {
      return saySomething;
   }

   public void setSaySomething(String saySomething) {
      this.saySomething = saySomething;
   }

   public String getHelloTypeCode() {
      return helloTypeCode;
   }

   public void setHelloTypeCode(String helloTypeCode) {
      this.helloTypeCode = helloTypeCode;
   }

   public Date getHelloDate() {
      return helloDate;
   }

   public void setHelloDate(Date helloDate) {
      this.helloDate = helloDate;
   }
}
