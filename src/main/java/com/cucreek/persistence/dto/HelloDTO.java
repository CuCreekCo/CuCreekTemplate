package com.cucreek.persistence.dto;

import com.cucreek.persistence.entity.HelloEntity;

import java.util.Date;

/**
 * Sample Hello DTO
 *
 * @author jljdavidson
 *
 */
public class HelloDTO extends BasePersistenceDTO {

   private Long helloId;
   private String saySomething;
   private CodeTableDTO helloTypeCode;
   private CodeTableDTO userRoleCode;
   private Date helloDate;


   public HelloDTO(){
      super();
   }
   public HelloDTO(HelloEntity helloEntity){
      this.helloId = helloEntity.getHelloId();
      this.saySomething = helloEntity.getSaySomething();
   }

   public Long getHelloId() {
      return helloId;
   }

   public void setHelloId(Long helloId) {
      this.helloId = helloId;
   }

   public String getSaySomething() {
      return saySomething;
   }

   public void setSaySomething(String saySomething) {
      this.saySomething = saySomething;
   }

   public Date getHelloDate() {
      return helloDate;
   }

   public void setHelloDate(Date helloDate) {
      this.helloDate = helloDate;
   }

   public CodeTableDTO getUserRoleCode() {
      return userRoleCode;
   }

   public void setUserRoleCode(CodeTableDTO userRoleCode) {
      this.userRoleCode = userRoleCode;
   }

   public CodeTableDTO getHelloTypeCode() {
      return helloTypeCode;
   }

   public void setHelloTypeCode(CodeTableDTO helloTypeCode) {
      this.helloTypeCode = helloTypeCode;
   }
}
