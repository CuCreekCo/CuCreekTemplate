package com.cucreek.common;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


/**
 * General business exception
 * 
 * @author jljdavidson
 *
 */
public class BusinessException extends Exception
{
   private static final long serialVersionUID = 5181805077222616350L;

   String constraintMessage = null;

   public BusinessException(String message, Throwable t)
   {
      super(message, t);
   }

   public BusinessException(String message)
   {
      super(message);
   }

   /**
    * Unwrap a constraint violation and make it useful
    * @param e
    */
   public BusinessException(ConstraintViolationException e)
   {
      for(ConstraintViolation<?> cv :e.getConstraintViolations())
      {
         constraintMessage = cv.getRootBean().getClass().getSimpleName()+"."+cv.getPropertyPath().toString()+" "+
                  cv.getMessage()+"\n";
      }
   }
   
   @Override
   public String getMessage()
   {
      if (StringUtils.isNotEmpty(constraintMessage))
      {
         return constraintMessage;
      }
      return super.getMessage();
   }
}
