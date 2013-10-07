package com.cucreek.business.service;

import com.cucreek.common.BusinessException;
import com.cucreek.common.CodeTableEntityAnnotation;
import com.cucreek.persistence.dto.CodeTableDTO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Interface for the code table business methods
 *
 * @author jljdavidson
 */
public interface CodeTableAdminService
{

   public List<CodeTableDTO> findCodesByTable(Class<?> clazz, Boolean includeEndDatedCodes) throws BusinessException;

   public CodeTableDTO findCodeByCode(Class<?> clazz, String codeValue, Boolean includeEndDatedCode) throws BusinessException;

   CodeTableDTO findCodeById(Class<?> clazz, Long codeId) throws BusinessException;

   /*
       * Helper method to convert a DFS JPA Entity code table to a CodeTableDTO
       *
       */
   CodeTableDTO convertEntityToCodeTableDTO(Object entity,
         CodeTableEntityAnnotation codeTableAnnotation)
         throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

   List<CodeTableDTO> findCodeBySearchTerm(Class<?> clazz, String searchTerm,
         Boolean includeEndDatedCodes, String sortField, String sortOrder, int... rowStartIdxAndCount) throws
         BusinessException;

   Object saveCreateCodeTableEntityObject(Class clazz, CodeTableDTO codeTableDTO) throws BusinessException;

   List<CodeTableDTO> findAllCodeTables();

   @Transactional(propagation = Propagation.REQUIRED)
   CodeTableDTO saveCodeTable(String codeTableName, CodeTableDTO codeTableDTO) throws BusinessException;
}
