package com.cucreek.web.controller;

import com.cucreek.common.SystemConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Base Controller class for all web requests.  This class provides helper
 * methods and classes for handling web requests.
 * 
 * @author jljdavidson
 *
 */
public abstract class BaseController implements Serializable
{
   private static final long serialVersionUID = -3827808231882779513L;

   Logger _logger = Logger.getLogger(BaseController.class);

   @Autowired
   MessageSource messageSource;
   
   public final static Long RESULT_LIST_SIZE = 20L;
   
   /**
    * Init binder to help with object conversion types - mostly dates and
    * collections of CodeTableDTO
    * 
    * @param binder
    */
   @InitBinder
   public void initBinder(WebDataBinder binder)
   {
       SimpleDateFormat dateFormat = new SimpleDateFormat(SystemConstants.DATE_FORMAT_DISPLAY);
       dateFormat.setLenient(false);
       binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
   }

   /**
    * Wrapper for Spring Message Source bean to simplify retrieving messages from dfs-messages and other
    * messages property files
    * 
    * @param messageId
    * @param replacements
    * @return Resolved message
    */
   public String messageSource(String messageId, String... replacements)
   {
      if (replacements!=null)
      {
         return
               StringUtils.normalizeSpace(new MessageSourceAccessor(messageSource).getMessage(messageId,replacements));
      }
      else
      {
         return StringUtils.normalizeSpace(new MessageSourceAccessor(messageSource).getMessage(messageId));
      }      
   }

   /**
    * Does the logged in user have a given role.
    * 
    * @param role
    * @return true or false
    */
   public boolean hasRole(String role)
   {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication==null) return false;

      for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
         if(StringUtils.equalsIgnoreCase(grantedAuthority.getAuthority(),role)){
            return true;
         }
      }
      return false;
   }
   
   /**
    * Helper method to get the URL of this app WITHOUT the context - just a bare:
    *    http://www.passwick.com
    *
    *
    * @param request http request
    * @param convertToHttps add an 's' to the http
    * @return the url
    */
   public StringBuffer getWebUrl(HttpServletRequest request, Boolean convertToHttps)
   {
      StringBuffer retVal;

      if (StringUtils.isEmpty(request.getContextPath()))
      {
         retVal = new StringBuffer().append(
               StringUtils.substringBefore(request.getRequestURL().toString(),
                                           request.getRequestURI()));
      }
      else
      {
         retVal = new StringBuffer().append(
               StringUtils.substringBefore(request.getRequestURL().toString(),request.getContextPath()));
      }

      if(convertToHttps)
      {
         int hi = retVal.indexOf("http://");
         if (hi>=0)
         {
            retVal.insert(hi+4,"s");
         }
      }
      return retVal;

   }

   /**
    * Helper method to get the URL of this app including the
    * servlet context path:
    *    http://www.passwick.com/servletcontextpath
    *
    *
    * @param request http request
    * @param convertToHttps add an 's' to the http protocol
    * @return url as string buffer
    */
   public StringBuffer getWebUrlWithContextPath(HttpServletRequest request, Boolean convertToHttps)
   {

      StringBuffer retVal = this.getWebUrl(request,convertToHttps);
      if (!StringUtils.isEmpty(request.getContextPath()))
      {
         retVal.append(request.getContextPath());
      }
      if(convertToHttps)
      {
         int hi = retVal.indexOf("http://");
         if (hi>=0)
         {
            retVal.insert(hi+4,"s");
         }
      }
      return retVal;

   }

}
