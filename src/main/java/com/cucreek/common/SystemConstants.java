package com.cucreek.common;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * Constant holder class.
 * 
 * @author jljdavidson
 *
 */
public abstract class SystemConstants
{
   public static final String JPA_ENTITY_PACKAGE = "com.cucreek.persistence.entity";

   public static final String ROLE_CD_HELLO = "ROLE_HELLO";

   public static final String DATE_FORMAT_DISPLAY = "MM/dd/yyyy";
   public static final String DATE_FORMAT_STORAGE = "yyyyMMdd";
   public static final String TIME_FORMAT_DISPLAY = "hh:mm aa";
   public static final String TIME_REGEXP = "^(1[0-2]|0[1-9]):[0-5][0-9] (AM|am|PM|pm)$";

   public static final String[] DATE_FORMATS =
         new String[]{
               DateFormatUtils.ISO_DATE_FORMAT.getPattern(),
               DateFormatUtils.ISO_DATETIME_FORMAT.getPattern(),
               DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(),               
               "MM/dd/yyyy HH:mm:ss",
               "MM/dd/yyyy hh:mm aa",
               "yyyy-MM-dd'T'HH:mmZ",
               DATE_FORMAT_DISPLAY,
               DATE_FORMAT_STORAGE};

   public static final String START_OF_DAY_TIME = "08:00 AM";
   public static final String END_OF_DAY_TIME = "05:00 PM";

   /**
    * Convenience method to split a value from SomeCamelCase to Some Camel Case
    *
    * @param s camel case string to split
    * @return string value
    */
   public static String splitCamelCase(String s) {
      return s.replaceAll(
            String.format("%s|%s|%s",
                  "(?<=[A-Z])(?=[A-Z][a-z])",
                  "(?<=[^A-Z])(?=[A-Z])",
                  "(?<=[A-Za-z])(?=[^A-Za-z])"
            ),
            " "
      );
   }


}
