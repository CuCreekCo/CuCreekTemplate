package com.cucreek.persistence.dto;

/**
 * Helper class to store validation messages
 *
 * @author jljdavidson
 */
public class ValidationMessageVO extends BasePersistenceDTO {
   private static final long serialVersionUID = -62550641069964079L;

   public enum ValidationMessageStatusEnum {
      SUCCESS, WARNING, ERROR;
   }

   private ValidationMessageStatusEnum status;
   private String message;
   private String entity;
   private String path;

   public ValidationMessageVO(ValidationMessageStatusEnum status, String message) {
      this.status = status;
      this.message = message;
   }

   public String getStatus() {
      return status.toString();
   }

   public String getMessage() {
      return message;
   }

   public String getEntity() {
      return entity;
   }

   public ValidationMessageVO setEntity(String entity) {
      this.entity = entity;
      return this;
   }

   public String getPath() {
      return path;
   }

   public ValidationMessageVO setPath(String path) {
      this.path = path;
      return this;
   }
}
