/**
 *
 */
package com.cucreek.business.service;

import com.cucreek.common.SystemConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;

/**
 * Base abstract class that the business services extend from.
 * <p/>
 * Provides convenience methods.
 *
 * @author jljdavidson
 */
public abstract class BaseBusinessService {
   @Autowired
   MessageSource businessMessageSource;

   /**
    * Helper method to format a DFS string stored date
    *
    * @param stringDate   string date
    * @param returnFormat the return format
    * @return String date in returnFormat or null if not parseable.
    */
   public static String formatStringDate(String stringDate, String returnFormat) {
      if (StringUtils.isNotEmpty(stringDate)) {
         try {
            return
                  DateFormatUtils.format(
                        DateUtils.parseDate(stringDate,
                              SystemConstants.DATE_FORMAT_STORAGE,
                              SystemConstants.DATE_FORMAT_DISPLAY),
                        returnFormat);
         }
         catch (ParseException e) {
            ;
         }
      }
      return null;
   }

   /**
    * Helper method to create a comma separated name
    *
    * @param lastName
    * @param firstName
    * @return lastname comma firstname
    */
   public static String lastNameCommaFirst(String lastName, String firstName) {
      assert (StringUtils.isNotEmpty(lastName));

      if (StringUtils.isNotEmpty(firstName)) {
         return lastName + ", " + firstName;
      }
      return lastName;
   }

   /**
    * Helper method to get a business message
    */
   public String getMessage(String code, String... replacements) {
      return new MessageSourceAccessor(businessMessageSource).
                                                                   getMessage(code, replacements);
   }

   /**
    * Get the currently logged in user.  If null, likely from JUnit context
    */
   public String getAuditUser() {
      if (SecurityContextHolder.getContext() == null ||
            SecurityContextHolder.getContext().getAuthentication() == null) {
         return "AUDIT_USER";
      }
      return SecurityContextHolder.getContext().getAuthentication().getName();
   }

   /**
    * Does the logged in user have a role?
    *
    * @param roleCode code
    * @return true or false
    */
   public Boolean userHasRole(String roleCode) {
      if (SecurityContextHolder.getContext() == null ||
            SecurityContextHolder.getContext().getAuthentication() == null) {
         return false;
      }
      try {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication==null) return false;

         for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if(StringUtils.equalsIgnoreCase(grantedAuthority.getAuthority(), roleCode)){
               return true;
            }
         }
         return false;
      }
      catch (Exception e) {
         return false;
      }
   }
}
