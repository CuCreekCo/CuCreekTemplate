package com.cucreek.common;

import com.cucreek.business.service.CodeTableAdminService;
import com.cucreek.persistence.dto.CodeTableDTO;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * A custom Spring MVC init binding property editor to handle 
 * the fact that things like checkboxes on a form are passed as
 * their string code value.  This converts to and from a String
 * to the CodeTableDTO.
 * 
 * @author jljdavidson
 *
 */
public class CodeTablePropertyEditor extends PropertyEditorSupport
{
   CodeTableAdminService codeTableAdminService;

   private Class<?> codeTableClass;

   public CodeTablePropertyEditor(Class<?> codeTableClass, CodeTableAdminService codeTableAdminService)
   {
      this.codeTableClass = codeTableClass;
      this.codeTableAdminService = codeTableAdminService;
   }
   
   @Override
   public String getAsText()
   {
      if (getValue()==null) return null;
      return ((CodeTableDTO) getValue()).getCodeValue();
   }

   @Override
   public void setAsText(String codeValue) throws IllegalArgumentException
   {
      try
      {
         if (StringUtils.isNotEmpty(codeValue))
         {
            setValue(codeTableAdminService.findCodeByCode(codeTableClass, codeValue, false));
         }
         else
         {
            setValue(null);
         }
      }
      catch (BusinessException e)
      {
         throw new IllegalArgumentException(e.getMessage(),e);
      }
   }
}
