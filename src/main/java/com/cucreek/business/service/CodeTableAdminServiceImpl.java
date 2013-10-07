/**
 *
 */
package com.cucreek.business.service;

import com.cucreek.common.BusinessException;
import com.cucreek.common.CodeTableEntityAnnotation;
import com.cucreek.common.CodeTableEntityFinder;
import com.cucreek.common.SystemConstants;
import com.cucreek.persistence.dto.BasePersistenceDTO;
import com.cucreek.persistence.dto.CodeTableDTO;
import com.cucreek.persistence.repository.CodeTableRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.eclipse.persistence.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Provides the business processes, and CRUD for the DFS code tables.
 *
 * @author jljdavidson
 */
@Service
@Transactional(readOnly = true)
public class CodeTableAdminServiceImpl extends BaseBusinessService implements CodeTableAdminService {
   Logger _logger = Logger.getLogger(CodeTableAdminService.class);
   @Autowired
   CodeTableRepository codeTableRepository;

   /*
    * Helper method to convert a DFS JPA Entity code table to a CodeTableDTO
    * 
    */
   @Override
   public CodeTableDTO convertEntityToCodeTableDTO(Object entity,
         CodeTableEntityAnnotation codeTableAnnotation)
         throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      String description = (String) MethodUtils.
                                                     invokeExactMethod(entity, "get" + StringUtils
                                                           .capitalize(
                                                                 codeTableAnnotation.description()));

      Object code = MethodUtils.
                                     invokeExactMethod(entity, "get" + StringUtils
                                           .capitalize(codeTableAnnotation.code()));
      String codeString;
      if (code.getClass().isAssignableFrom(Long.class)) {
         codeString = code.toString();
      }
      else {
         codeString = (String) code;
      }
      Date effective = (Date) MethodUtils.
                                               invokeExactMethod(entity, "get" + StringUtils
                                                     .capitalize(
                                                           codeTableAnnotation.effectiveDate()));

      Date end =
            StringUtils.isEmpty(codeTableAnnotation.endDate()) ? null :
            (Date) MethodUtils.
                                    invokeExactMethod(entity, "get" + StringUtils
                                          .capitalize(codeTableAnnotation.endDate()));

      return new CodeTableDTO(codeString, description, effective, end);
   }

   /**
    * Return a list of code tables as a list of code table DTOs (meta, bro).
    *
    * @return Code tables in CodeTableDTO format
    */
   @Override
   public List<CodeTableDTO> findAllCodeTables() {
      return new CodeTableEntityFinder().getCodeTableList();
   }

   /**
    * Given a code table class, find all rows and return a list of CodeTableDTO list.
    * <p/>
    * A code table is an entity annotated with DfsCodeTable.  If the passed in clazz does not have this annotation an
    * exception is thrown.
    *
    * @param clazz                of the code table
    * @param includeEndDatedCodes end dated codes?  Default is do NOT include end dated codes.
    * @return list of CodeTableDTOs for the passed in code table entity
    * @throws BusinessException
    */
   @Override
   public List<CodeTableDTO> findCodesByTable(Class<?> clazz, Boolean includeEndDatedCodes) throws
         BusinessException {

      if (clazz == null) {
         throw new BusinessException(
               getMessage("error.0003.invalid.code.table",
                     new String[]{""}));
      }

      CodeTableEntityAnnotation codeTableAnnotation =
            AnnotationUtils.findAnnotation(clazz, CodeTableEntityAnnotation.class);

      if (codeTableAnnotation == null) {
         throw new BusinessException(
               getMessage("error.0003.invalid.code.table",
                     new String[]{clazz.getName()}));
      }
      List<?> codeList = codeTableRepository.findAll(clazz);
      List<CodeTableDTO> ctvList = new ArrayList<CodeTableDTO>();

      try {
         for (Object code : codeList) {
            CodeTableDTO ctd = this.convertEntityToCodeTableDTO(code, codeTableAnnotation);
            if (includeEndDatedCodes || ctd.getEndDate() == null &&
                  ctd.getEffectiveDate().before(Calendar.getInstance().getTime())) {
               ctvList.add(ctd);
            }
         }
      }
      catch (Exception e) {
         _logger.error(e.getMessage(), e);
         throw new BusinessException(e.getMessage());
      }
      Collections.sort(ctvList, new Comparator<CodeTableDTO>() {
         @Override
         public int compare(CodeTableDTO o1, CodeTableDTO o2) {
            return o1.getDescription().compareTo(o2.getDescription());
         }
      });
      return ctvList;
   }

   /**
    * Given a code value return CodeTableDTO
    *
    * @param clazz               entity class to find
    * @param codeValue           code value to lookup
    * @param includeEndDatedCode disregard end date on lookup
    * @throws BusinessException
    * @throws org.springframework.context.NoSuchMessageException
    *
    */
   @Override
   public CodeTableDTO findCodeByCode(Class<?> clazz, String codeValue, Boolean includeEndDatedCode) throws
         BusinessException {

      if (clazz == null) {
         throw new BusinessException(
               getMessage("error.0003.invalid.code.table",
                     new String[]{""}));
      }

      CodeTableEntityAnnotation codeTableAnnotation =
            AnnotationUtils.findAnnotation(clazz, CodeTableEntityAnnotation.class);

      if (codeTableAnnotation == null) {
         throw new BusinessException(
               getMessage("error.0003.invalid.code.table",
                     new String[]{clazz.getName()}));
      }

      try {
         Object codeValueObject = codeValue;
         if (!StringUtils.equalsIgnoreCase(codeTableAnnotation.convertCodeToLongOnLookup(), "no")) {
            if (codeValue == null) {
               codeValueObject = null;
            }
            else {
               codeValueObject = Long.valueOf(codeValue);
            }
         }

         List<?> codeList =
               codeTableRepository.findByProperty(clazz, codeTableAnnotation.code(), codeValueObject);
         CodeTableDTO retVal = null;
         for (Object obj : codeList) {
            Date end = StringUtils.isEmpty(codeTableAnnotation.endDate()) ? null : (Date) MethodUtils.
                                                                                                           invokeExactMethod(
                                                                                                                 obj,
                                                                                                                 "get" + StringUtils
                                                                                                                       .capitalize(
                                                                                                                             codeTableAnnotation
                                                                                                                                   .endDate()));
            if (end == null || includeEndDatedCode) {
               retVal = this.convertEntityToCodeTableDTO(obj, codeTableAnnotation);
               break;
            }
         }
         return retVal;
      }
      catch (Exception e) {
         _logger.error(e.getMessage(), e);
         throw new BusinessException(e.getMessage());
      }
   }

   /**
    * Given a code value return NON END DATED CodeTableDTO
    *
    * @throws BusinessException
    * @throws org.springframework.context.NoSuchMessageException
    *
    */
   @Override
   public CodeTableDTO findCodeById(Class<?> clazz, Long codeId) throws BusinessException {

      if (clazz == null) {
         throw new BusinessException(
               getMessage("error.0003.invalid.code.table",
                     new String[]{""}));
      }

      CodeTableEntityAnnotation codeTableAnnotation =
            AnnotationUtils.findAnnotation(clazz, CodeTableEntityAnnotation.class);

      if (codeTableAnnotation == null) {
         throw new BusinessException(
               getMessage("error.0003.invalid.code.table",
                     new String[]{clazz.getName()}));
      }

      try {

         Object obj = codeTableRepository.findById(clazz, codeId);

         return this.convertEntityToCodeTableDTO(obj, codeTableAnnotation);
      }
      catch (Exception e) {
         _logger.error(e.getMessage(), e);
         throw new BusinessException(e.getMessage());
      }
   }

   /**
    * Given a search term find a code by partially matching on the code value, description
    *
    * @throws BusinessException
    * @throws org.springframework.context.NoSuchMessageException
    *
    */
   @Override
   public List<CodeTableDTO> findCodeBySearchTerm(Class<?> clazz, String searchTerm,
         Boolean includeEndDatedCodes, String sortField, String sortOrder, int... rowStartIdxAndCount) throws
         BusinessException {

      if (clazz == null) {
         throw new BusinessException(
               getMessage("error.0003.invalid.code.table",
                     new String[]{""}));
      }

      CodeTableEntityAnnotation codeTableAnnotation =
            AnnotationUtils.findAnnotation(clazz, CodeTableEntityAnnotation.class);

      if (codeTableAnnotation == null) {
         throw new BusinessException(
               getMessage("error.0003.invalid.code.table",
                     new String[]{clazz.getName()}));
      }

      try {
         return codeTableRepository.findCodeTableBySearch(clazz, codeTableAnnotation.code(),
               codeTableAnnotation.description(),
               codeTableAnnotation.effectiveDate(),
               codeTableAnnotation.endDate(),
               searchTerm, includeEndDatedCodes,
               sortField,
               sortOrder,
               rowStartIdxAndCount);
      }
      catch (Exception e) {
         _logger.error(e.getMessage(), e);
         throw new BusinessException(e.getMessage());
      }
   }

   /**
    * Given a CodeTableDTO and a CodeTableEntityAnnotation annotated class, create a new row or update an existing.
    * Requires an existing transaction BECAUSE A CODE TABLE MAY HAVE A PARENT OBJECT REQUIREMENT SO THIS METHOD CANNOT
    * BLINDLY COMMIT.
    * <p/>
    * !!  THIS METHOD DOES NOT ISSUE AN entityManager.save BECAUSE OF THAT !!
    * <p/>
    * Instead, it delegates that to its caller (think a zip code table and its dependence on a state code table).
    *
    * @param clazz        is the JPA entity code table class
    * @param codeTableDTO to save
    * @return saved (not committed) JPA entity of type clazz
    */
   @Override
   public Object saveCreateCodeTableEntityObject(Class clazz, CodeTableDTO codeTableDTO) throws BusinessException {
      if (clazz == null) {
         throw new BusinessException(
               getMessage("error.0003.invalid.code.table",
                     new String[]{""}));
      }

      CodeTableEntityAnnotation codeTableAnnotation =
            AnnotationUtils.findAnnotation(clazz, CodeTableEntityAnnotation.class);

      if (codeTableAnnotation == null) {
         throw new BusinessException(
               getMessage("error.0003.invalid.code.table",
                     new String[]{clazz.getName()}));
      }

      Date now = Calendar.getInstance().getTime();

      CodeTableDTO modifiedCodeTableDTO;
      Object lookupValue;

      if (StringUtils.isNotEmpty(codeTableDTO.getOldCodeValue()) &&
            !StringUtils.equalsIgnoreCase(codeTableDTO.getCodeValue(), codeTableDTO.getOldCodeValue())) {
         modifiedCodeTableDTO = this.findCodeByCode(clazz, codeTableDTO.getOldCodeValue(), true);
         lookupValue = codeTableDTO.getOldCodeValue();
      }
      else if (codeTableDTO.getChangeStatus().equals(BasePersistenceDTO.Status.INSERT)) {
         modifiedCodeTableDTO = this.findCodeByCode(clazz, codeTableDTO.getCodeValue(), true);
         lookupValue = StringUtils.upperCase(codeTableDTO.getCodeValue());
         if (modifiedCodeTableDTO != null) {
            throw new BusinessException(
                  getMessage("error.0004.duplicate.row", codeTableDTO.getCodeValue()));
         }
      }
      else {
         modifiedCodeTableDTO = this.findCodeByCode(clazz, codeTableDTO.getCodeValue(), true);
         lookupValue = StringUtils.upperCase(codeTableDTO.getCodeValue());
      }
      try {
         if (!StringUtils.equalsIgnoreCase(codeTableAnnotation.convertCodeToLongOnLookup(), "no")) {
            if (lookupValue != null) {
               lookupValue = Long.valueOf((String) lookupValue);
            }
         }

         Boolean isNewRow = false;
         Object newObject;
         if (modifiedCodeTableDTO == null) //new row
         {
            newObject = clazz.newInstance();
            isNewRow = true;
         }
         else {
            newObject = codeTableRepository.findById(clazz, lookupValue);
         }

         if (lookupValue != null) {
            /* Changing from an old code to a new code */
            if (!StringUtils.equals(codeTableDTO.getOldCodeValue(), codeTableDTO.getCodeValue())) {
               MethodUtils.invokeExactMethod(
                     newObject,
                     "set" + StringUtils.capitalize(codeTableAnnotation.code()), codeTableDTO.getCodeValue());

            }
            else {
               MethodUtils.invokeExactMethod(
                     newObject,
                     "set" + StringUtils.capitalize(codeTableAnnotation.code()), lookupValue);
            }
         }

         if (codeTableDTO.getDescription() != null) {
            MethodUtils.invokeExactMethod(
                  newObject,
                  "set" + StringUtils.capitalize(codeTableAnnotation.description()), codeTableDTO.getDescription());
         }

         if (codeTableDTO.getEffectiveDate() != null) {
            MethodUtils.invokeExactMethod(
                  newObject,
                  "set" + StringUtils.capitalize(codeTableAnnotation.effectiveDate()), codeTableDTO.getEffectiveDate());
         }

         if (!StringUtils.isEmpty(codeTableAnnotation.endDate())) {
            if (codeTableDTO.getEndDate() != null) {
               MethodUtils.invokeExactMethod(
                     newObject,
                     "set" + StringUtils.capitalize(codeTableAnnotation.endDate()), codeTableDTO.getEndDate());
            }
            else {
               Object[] args = new Object[]{null};
               Class[] classArgs = new Class[]{Date.class};
               MethodUtils.invokeExactMethod(
                     newObject,
                     "set" + StringUtils.capitalize(codeTableAnnotation.endDate()), args, classArgs);
            }
         }

         /*
         find the audit fields
          */
         for (Method method : clazz.getMethods()) {
            if (isNewRow) {
               if (StringUtils.endsWith(method.getName(), "DateCreated") &&
                     StringUtils.startsWith(method.getName(), "set")) {
                  MethodUtils.invokeExactMethod(
                        newObject, method.getName(), now);
               }
               else if (StringUtils.endsWith(method.getName(), "CreatedBy") &&
                     StringUtils.startsWith(method.getName(), "set")) {
                  MethodUtils.invokeExactMethod(
                        newObject, method.getName(), getAuditUser());
               }
            }
            else {
               if (StringUtils.endsWith(method.getName(), "DateModified") &&
                     StringUtils.startsWith(method.getName(), "set")) {
                  MethodUtils.invokeExactMethod(
                        newObject, method.getName(), now);
               }
               else if (StringUtils.endsWith(method.getName(), "ModifiedBy") &&
                     StringUtils.startsWith(method.getName(), "set")) {
                  MethodUtils.invokeExactMethod(
                        newObject, method.getName(), getAuditUser());
               }
            }
         }
         return newObject;
      }
      catch (InstantiationException e) {
         throw new BusinessException(e.getMessage(), e);
      }
      catch (IllegalAccessException e) {
         throw new BusinessException(e.getMessage(), e);
      }
      catch (NoSuchMethodException e) {
         throw new BusinessException(e.getMessage(), e);
      }
      catch (InvocationTargetException e) {
         throw new BusinessException(e.getMessage(), e);
      }
   }

   /**
    * Given a code table name, and the code tables value object save it to the database.  If the code table object is
    * marked as INSERT then it will be stored anew.  Otherwise the objects old code value and new code value will be
    * compared.  If they differ, the it will be looked up by the old value and modified to the new values.  Duplicate
    * checks are in place.
    *
    * @param codeTableName code table name
    * @param codeTableDTO  code table object
    * @return code table object.
    * @throws BusinessException
    */
   @Override
   @Transactional(propagation = Propagation.REQUIRED)
   public CodeTableDTO saveCodeTable(String codeTableName, CodeTableDTO codeTableDTO) throws BusinessException {

      try {
         /* Clean up the user input */
         codeTableDTO.setCodeValue(StringUtils.trim(StringUtils.upperCase(codeTableDTO.getCodeValue())));
         codeTableDTO.setDescription(StringUtils.trim(WordUtils.capitalize(codeTableDTO.getDescription())));

         /* Validate the code table */
         codeTableDTO.clearMessages();

         Class codeTableClass = Class.forName(SystemConstants.JPA_ENTITY_PACKAGE + "." + codeTableName);
         if (StringUtils.isBlank(codeTableDTO.getCodeValue())) {
            codeTableDTO.addErrorMessage(CodeTableDTO.class.getSimpleName(), "codeValue",
                  getMessage("error.0001.required.field"));
         }
         if (StringUtils.isBlank(codeTableDTO.getDescription())) {
            codeTableDTO.addErrorMessage(CodeTableDTO.class.getSimpleName(), "description",
                  getMessage("error.0001.required.field"));
         }

         if (!codeTableDTO.getValidationMessages().isEmpty()) {
            return codeTableDTO;
         }
         Object newEntityObject = this.saveCreateCodeTableEntityObject(codeTableClass, codeTableDTO);
         codeTableRepository.save(newEntityObject);
         return codeTableDTO;
      }
      catch (ValidationException e) {
         throw new BusinessException(e.getMessage(), e);
      }
      catch (ClassNotFoundException e) {
         throw new BusinessException(e.getMessage(), e);
      }
   }
}
