package com.cucreek.persistence.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base data transfer object for the application.
 *
 * @author jljdavidson
 */
public abstract class BasePersistenceDTO implements Serializable {
   private static final long serialVersionUID = 3690088495755324797L;

   public enum Status {
      QUERY, UPDATE, DELETE, INSERT, ERROR, CONVERSION;
   }

   private List<ValidationMessageVO> validationMessages = new ArrayList<ValidationMessageVO>();
   private Status changeStatus = Status.QUERY;
   @JsonIgnore
   private Boolean selected;


   /**
    * Does this DTO have error messages?
    *
    * @return yay or nay
    */
   public Boolean hasErrorMesssages() {
      if (getValidationMessages() != null && !getValidationMessages().isEmpty()) {
         for (ValidationMessageVO validationMessageVO : getValidationMessages()) {
            if(validationMessageVO.getStatus().equals(ValidationMessageVO.ValidationMessageStatusEnum.ERROR)){
               return true;
            }
         }
      }
      return false;
   }
   /**
    * Helper method to add validation message
    *
    * @param entity
    * @param path
    * @param message
    * @param status
    */
   private void addValidationMessage(String entity, String path, String message, ValidationMessageVO.ValidationMessageStatusEnum status) {
      this.validationMessages.add(new ValidationMessageVO(status, message).setEntity(entity).setPath(path));
   }

   /**
    * Add an error message to this DTOs validation message list
    *
    * @param entity
    * @param path
    * @param message
    */
   public void addErrorMessage(String entity, String path, String message) {
      addValidationMessage(entity, path, message, ValidationMessageVO.ValidationMessageStatusEnum.ERROR);
   }

   /**
    * Add an warning message to this DTOs validation message list
    *
    * @param entity
    * @param path
    * @param message
    */
   public void addWarningMessage(String entity, String path, String message) {
      addValidationMessage(entity, path, message, ValidationMessageVO.ValidationMessageStatusEnum.WARNING);
   }

   /**
    * Add an success message to this DTOs validation message list
    *
    * @param entity
    * @param path
    * @param message
    */
   public void addSuccessMessage(String entity, String path, String message) {
      addValidationMessage(entity, path, message, ValidationMessageVO.ValidationMessageStatusEnum.SUCCESS);
   }

   /**
    * Clear out the validation messages in this object
    */
   public void clearMessages() {
      setValidationMessages(new ArrayList<ValidationMessageVO>());
   }

   public List<ValidationMessageVO> getValidationMessages() {
      return validationMessages;
   }

   public void setValidationMessages(List<ValidationMessageVO> validationMessages) {
      this.validationMessages = validationMessages;
   }

   public Status getChangeStatus() {
      return changeStatus;
   }

   public void setChangeStatus(Status changeStatus) {
      this.changeStatus = changeStatus;
   }

   public Boolean getSelected() {
      return selected;
   }

   public void setSelected(Boolean selected) {
      this.selected = selected;
   }
}
