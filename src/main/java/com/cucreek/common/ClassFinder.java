package com.cucreek.common;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

/**
 * Class Finder class that searches packages for
 * Java classes that match a particular criteria.
 *
 * @author jljdavidson
 */
public abstract class ClassFinder {
   /**
    * The String constant that is the bin path that is used with Bundles to
    * find the Java class entries.
    */
   public static final String BUNDLE_PATH_BIN = "bin/";

   /**
    * The String constant that is the class type that is used to find the Java
    * class entries.
    */
   public static final String CLASS_TYPE = ".class";

   /**
    * The Context Object to be used with Bundles to find the Java class
    * entries.
    */
   private Object contextObject = null;

   /**
    * The String path to be used in the search to find the Java class entries.
    */
   private String searchPath = null;

   /**
    * Searches the classpath for all classes matching a specified search
    * criteria, returning them in a map keyed with the packages they are in.
    * The search criteria can be specified via interface, package and jar name
    * filter arguments
    * <p>
    *
    * @param classLoader
    *            The classloader whose classpath will be traversed
    * @param interfaceFilter
    *            A Set of fully qualified interface names to search for or null
    *            to return classes implementing all interfaces
    * @param packageFilter
    *            A Set of fully qualified package names to search for or or
    *            null to return classes in all packages
    * @param jarFilter
    *            A Set of jar file names to search for or null to return
    *            classes from all jars
    * @param packagePrefix
    *            A String package prefix name to search for or null to return
    *            classes in all packages
    * @return A Map of a Set of Classes keyed to their package names
    */
   public Map<String, Set<Class<?>>> findClasses(ClassLoader classLoader, Set<String> interfaceFilter, Set<String> packageFilter, Set<String> jarFilter, String packagePrefix) {
      Map<String, Set<Class<?>>> classTable = new HashMap<String, Set<Class<?>>>();
      Object[] classPaths = null;
      String urlClassPath = null;

      if (contextObject != null) {
         // the context Object has been set, so use it as the classpath
         classPaths = new Object[] { contextObject };
      } else {
         URLClassLoader urlClassLoader = null;

         if (classLoader instanceof URLClassLoader) {
            urlClassLoader = (URLClassLoader)classLoader;
         } else {
            Method getClssPthMth = null;

            try {
               getClssPthMth = classLoader.getClass().getMethod("getClassPath", (Class[])null);
            } catch (SecurityException e) {
               getClssPthMth = null;
            } catch (NoSuchMethodException e) {
               getClssPthMth = null;
            }

            Object getClssPthObj = null;

            if (getClssPthMth != null) {
               try {
                  getClssPthObj = getClssPthMth.invoke(classLoader, (Object[])null);
               } catch (IllegalArgumentException e) {
                  getClssPthObj = null;
               } catch (IllegalAccessException e) {
                  getClssPthObj = null;
               } catch (InvocationTargetException e) {
                  getClssPthObj = null;
               }
            }

            if (getClssPthObj instanceof String) {
               urlClassPath = getClssPthObj.toString();
            }
         }

         if ((urlClassLoader == null) && (urlClassPath == null)) {
            ClassLoader prntClassLoader = classLoader.getParent();

            while (prntClassLoader != null) {
               if (prntClassLoader instanceof URLClassLoader) {
                  urlClassLoader = (URLClassLoader)prntClassLoader;
                  break;
               }

               prntClassLoader = prntClassLoader.getParent();
            }
         }

         if (urlClassLoader != null) {
            try {
               // get a list of all classpaths
               classPaths = urlClassLoader.getURLs();
            } catch (ClassCastException cce) {
               // or cast failed; tokenize the system classpath
               classPaths = urlClassPath.split(File.pathSeparator);
            }
         }
      }

      if (urlClassPath == null) {
         urlClassPath = System.getProperty("java.class.path", "");
      }

      if (classPaths == null) {
         classPaths = urlClassPath.split(File.pathSeparator);
      }

      for (int h = 0; h < classPaths.length; h++) {
         Enumeration<?> files = null;
         JarFile module = null;

         // for each classpath ...
         File classPath = null;

         if (!classPaths[h].equals(contextObject)) {
            // the current classpath is not the same as the context Object
            // that could of been set, so process as a regular classpath
            classPath = new File((URL.class).isInstance(classPaths[h]) ? ((URL)classPaths[h]).getFile() : classPaths[h].toString());
         }

         boolean findFilesInContextObjectFlag = false;

         if (classPath != null && classPath.isDirectory() && jarFilter == null) {
            // is our classpath a directory and jar filters are not active?
            List<String> dirListing = new ArrayList<String>();
            StringBuilder dirPrefix = new StringBuilder();

            if (StringUtils.isNotBlank(packagePrefix)) {
               dirPrefix.append(packagePrefix.trim());
               int replcPos = dirPrefix.indexOf(".");

               while (replcPos > -1) {
                  dirPrefix.replace(replcPos, replcPos + 1, "|");
                  replcPos = dirPrefix.indexOf(".");
               }
            }

            // get a recursive listing of this classpath
            recursivelyListDir(dirListing, classPath, new StringBuffer(), dirPrefix.toString());
            // an enumeration wrapping our list of files
            files = Collections.enumeration(dirListing);
         } else if (classPath != null && classPath.getName().endsWith(".jar")) { // is our
            // classpath a jar? skip any jars not list in the filter
            if (jarFilter != null && !jarFilter.contains(classPath.getName())) {
               continue;
            }

            try {
               // if our resource is a jar, instantiate a jarfile using the
               // full path to resource
               module = new JarFile(URLDecoder.decode(classPath.getAbsolutePath(), "utf-8"));
            } catch (MalformedURLException mue) {
               continue;
            } catch (IOException io) {
               continue;
            }

            // get an enumeration of the files in this jar
            files = module.entries();
         }

         // for each file path in our directory or jar
         while (files != null && files.hasMoreElements()) {
            // get each fileName
            String fileName = files.nextElement().toString();

            // we only want the class files
            if (fileName.endsWith(CLASS_TYPE)) {
               // convert our full filename to a fully qualified class name
               StringBuilder className = new StringBuilder(fileName.substring(0, fileName.length() - 6));
               int replcFwdPos = className.indexOf("/");
               int replcBwdPos = className.indexOf("\\");

               while ((replcFwdPos > -1) || (replcBwdPos > -1)) {
                  if ((replcFwdPos > -1)) {
                     className.replace(replcFwdPos, replcFwdPos + 1, ".");
                  } else {
                     className.replace(replcBwdPos, replcBwdPos + 1, ".");
                  }

                  replcFwdPos = className.indexOf("/");
                  replcBwdPos = className.indexOf("\\");
               }

               if (className.lastIndexOf(".") < 0) {
                  continue;
               }

               String packageName = className.substring(0, className.lastIndexOf("."));

               // skip any classes in packages not explicitly requested in
               // our package filter
               if (packageFilter != null && !packageFilter.contains(packageName)) {
                  continue;
               } else if (StringUtils.isNotBlank(packagePrefix) && !packageName.startsWith(packagePrefix.trim())) {
                  continue;
               }

               // get the class for our class name
               Class<?> theClass = null;

               try {
                  theClass = Class.forName(className.toString(), false, classLoader);
               } catch (NoClassDefFoundError e) {
                  continue;
               } catch (ClassNotFoundException e) {
                  continue;
               }

               if (theClass.isInterface() || theClass.isAnnotation()) {
                  // Skip Interfaces and Annotations
                  continue;
               }

               boolean addFoundClass = false;

               if (interfaceFilter == null) {
                  // No Interface filtering required, so flag the found
                  // class to be added
                  addFoundClass = true;
               } else {
                  // Interface filtering required, so then get an array of
                  // all the interfaces in our class to be checked
                  Class<?>[] classInterfaces = theClass.getInterfaces();

                  for (int i = 0; i < classInterfaces.length; i++) {
                     String interfaceName = classInterfaces[i].getName();

                     // was this interface requested?
                     if (interfaceFilter.contains(interfaceName)) {
                        addFoundClass = true;
                        break;
                     }
                  }
               }

               if (addFoundClass) {
                  // is this package already in the map?
                  if (classTable.containsKey(packageName)) {
                     // if so then just add this class to the end of the
                     // list of classes contained in this package
                     classTable.get(packageName).add(theClass);
                  } else {
                     // else create a new list initialized with our first
                     // class and put the list into the map
                     Set<Class<?>> allClasses = new HashSet<Class<?>>();
                     allClasses.add(theClass);
                     classTable.put(packageName, allClasses);
                  }
               }
            }
         }

         // close the jar if it was used
         if (module != null) {
            try {
               module.close();
            } catch (IOException ioe) {
            }
         }
      } // end for loop

      return classTable;
   } // end method

   /**
    * Searches the classpath for all classes matching a specified search
    * criteria, and having a package name starting with the supplied prefix
    * String. Returning them in a map keyed with the packages they are in. The
    * search criteria can be specified via interface and jar name filter
    * arguments.
    * <p>
    *
    * @param classLoader
    *            The classloader whose classpath will be traversed
    * @param packagePrefix
    *            The String used in the search process to filter packages by a
    *            prefix.
    * @param interfaceFilter
    *            A Set of fully qualified interface names to search for or null
    *            to return classes implementing all interfaces
    * @param jarFilter
    *            A Set of jar file names to search for or null to return
    *            classes from all jars
    * @return A Map of a Set of Classes keyed to their package names
    */
   public Map<String, Set<Class<?>>> findClassesWithPackagePrefix(ClassLoader classLoader, String packagePrefix, Set<String> interfaceFilter, Set<String> jarFilter) {
      Map<String, Set<Class<?>>> fndClasses = findClasses(classLoader, interfaceFilter, null, jarFilter, packagePrefix);

      if ((fndClasses == null) || (fndClasses.size() == 0)) {
         return null;
      }

      // Setup the package name prefix to be tested in the package names
      // of the Class files found
      String pckgNamePre = "gov.mt.mdt";

      if (packagePrefix != null) {
         pckgNamePre = packagePrefix;
      }

      Object[] pckgNameKeys = fndClasses.keySet().toArray();

      for (int i = pckgNameKeys.length - 1; i >= 0; i--) {
         if (!pckgNameKeys[i].toString().equals(pckgNamePre) && !pckgNameKeys[i].toString().startsWith(pckgNamePre)) {
            fndClasses.remove(pckgNameKeys[i].toString());
         }
      }

      return fndClasses;
   }

   /**
    * Searches the classpath for all classes matching a specified search
    * criteria, and having a package name starting with the supplied prefix
    * String. Returning them in a map keyed with the packages they are in. The
    * search criteria can be specified via interface name filter argument.
    * <p>
    *
    * @param classLoader
    *            The classloader whose classpath will be traversed
    * @param packagePrefix
    *            The String used in the search process to filter packages by a
    *            prefix.
    * @param interfaceFilter
    *            A Set of fully qualified interface names to search for or null
    *            to return classes implementing all interfaces
    * @return A Map of a Set of Classes keyed to their package names
    */
   public Map<String, Set<Class<?>>> findClassesWithPackagePrefix(ClassLoader classLoader, String packagePrefix, Set<String> interfaceFilter) {
      return findClassesWithPackagePrefix(classLoader, packagePrefix, interfaceFilter, null);
   }

   /**
    * Searches the classpath for all classes having a package name starting
    * with the supplied prefix String. Returning them in a map keyed with the
    * packages they are in.
    * <p>
    *
    * @param classLoader
    *            The classloader whose classpath will be traversed
    * @param packagePrefix
    *            The String used in the search process to filter packages by a
    *            prefix.
    * @return A Map of a Set of Classes keyed to their package names
    */
   public Map<String, Set<Class<?>>> findClassesWithPackagePrefix(ClassLoader classLoader, String packagePrefix) {
      return findClassesWithPackagePrefix(classLoader, packagePrefix, null, null);
   }

   /**
    * Gets the context Object property.
    *
    * @return The Object context property.
    */
   public Object getContextObject() {
      return contextObject;
   }

   /**
    * Sets the context Object property.
    *
    * @param context
    *            The Object context to be set in the property.
    */
   public void setContextObject(Object context) {
      this.contextObject = context;
   }

   /**
    * Gets the search path String property.
    *
    * @return The String search path property.
    */
   public String getSearchPath() {
      return searchPath;
   }

   /**
    * Sets the search path String property.
    *
    * @param searchPath
    *            The String search path to be set in the property.
    */
   public void setSearchPath(String searchPath) {
      this.searchPath = searchPath;
   }

   /**
    * Checks the supplied Class to see if it matches particular criteria for
    * this BundleClassFinder class.
    *
    * @param clss
    *            The Class to be checked.
    * @return The boolean flag true if the supplied Class matches the
    *         particular criteria; otherwise false.
    */
   protected abstract boolean classMatchesCriteria(Class<?> clss);

   /**
    * Searches the classpath for all classes that return true from calling the
    * classMatchesCriteria method, and having a package name starting with the
    * supplied prefix String. Returning them in a map keyed with the packages
    * they are in. Additional search criteria can be specified via interface
    * and jar name filter arguments.
    * <p>
    *
    * @param classLoader
    *            The classloader whose classpath will be traversed
    * @param packagePrefix
    *            The String used in the search process to filter packages by a
    *            prefix.
    * @param interfaceFilter
    *            A Set of fully qualified interface names to search for or null
    *            to return classes implementing all interfaces
    * @param jarFilter
    *            A Set of jar file names to search for or null to return
    *            classes from all jars
    * @return A Map of a Set of Classes keyed to their package names
    */
   protected Map<String, Set<Class<?>>> findClassesWithPackagePrefixMatchingCriteria(ClassLoader classLoader, String packagePrefix, Set<String> interfaceFilter, Set<String> jarFilter) {
      Map<String, Set<Class<?>>> fndClasses = findClassesWithPackagePrefix(classLoader, packagePrefix, interfaceFilter, jarFilter);

      if ((fndClasses == null) || (fndClasses.size() == 0)) {
         return null;
      }

      for (String pckgName : fndClasses.keySet()) {
         Set<Class<?>> pckgClasses = fndClasses.get(pckgName);

         if ((pckgClasses != null) && (pckgClasses.size() > 0)) {
            Object[] theClasses = pckgClasses.toArray();

            for (int i = theClasses.length - 1; i >= 0; i--) {
               if (!(theClasses[i] instanceof Class<?>) || !classMatchesCriteria((Class<?>)theClasses[i])) {
                  // The Java Class does not match the particular
                  // criteria
                  pckgClasses.remove(theClasses[i]);
               }
            }

            fndClasses.put(pckgName, pckgClasses);
         }
      }

      return fndClasses;
   }

   /**
    * Searches the classpath for all classes that return true from calling the
    * classMatchesCriteria method, and having a package name starting with the
    * supplied prefix String. Returning them in a map keyed with the packages
    * they are in. Additional search criteria can be specified via interface
    * name filter argument.
    * <p>
    *
    * @param classLoader
    *            The classloader whose classpath will be traversed
    * @param packagePrefix
    *            The String used in the search process to filter packages by a
    *            prefix.
    * @param interfaceFilter
    *            A Set of fully qualified interface names to search for or null
    *            to return classes implementing all interfaces
    * @return A Map of a Set of Classes keyed to their package names
    */
   protected Map<String, Set<Class<?>>> findClassesWithPackagePrefixMatchingCriteria(ClassLoader classLoader, String packagePrefix, Set<String> interfaceFilter) {
      return findClassesWithPackagePrefixMatchingCriteria(classLoader, packagePrefix, interfaceFilter, null);
   }

   /**
    * Searches the classpath for all classes that return true from calling the
    * classMatchesCriteria method, and having a package name starting with the
    * supplied prefix String. Returning them in a map keyed with the packages
    * they are in.
    * <p>
    *
    * @param classLoader
    *            The classloader whose classpath will be traversed
    * @param packagePrefix
    *            The String used in the search process to filter packages by a
    *            prefix.
    * @return A Map of a Set of Classes keyed to their package names
    */
   protected Map<String, Set<Class<?>>> findClassesWithPackagePrefixMatchingCriteria(ClassLoader classLoader, String packagePrefix) {
      return findClassesWithPackagePrefixMatchingCriteria(classLoader, packagePrefix, null, null);
   }

   /**
    * Converts the Class's supplied String bundle path to a String package
    * name; optionally using the boolean flag that indicates if bin was added
    * to the beginning of the path.
    *
    * @param bundlePath
    *            The String Class's bundle path to be converted.
    * @param foundUsingBin
    *            The boolean flag that optionally indicates if bin was added to
    *            the beginning of the path.
    * @return The String Class's package name converted from the bundle path;

   import java.sql.Timestamp;
    *         or an empty String.
    */
   protected String convertClassBundlePathToPackageName(final String bundlePath, final boolean foundUsingBin) {
      StringBuffer pckgName = new StringBuffer(bundlePath);

      if (foundUsingBin) {
         int binPos = pckgName.indexOf(BUNDLE_PATH_BIN);

         if (binPos > -1) {
            pckgName.delete(0, binPos + BUNDLE_PATH_BIN.length());
         }
      }

      int pathStartPos = pckgName.indexOf("://");
      int pathStartSize = 3;

      if (pathStartPos > -1) {
         if (pckgName.indexOf("/", pathStartPos + 3) > pathStartPos) {
            pathStartPos = pckgName.indexOf("/", pathStartPos + 3);
            pathStartSize = 1;
         }

         if (pathStartPos > -1) {
            pckgName.delete(0, pathStartPos + pathStartSize);
         }
      }

      if (pckgName.toString().startsWith("/")) {
         pckgName.delete(0, 1);
      }

      return pckgName.toString().replace('/', '.');
   }

   /**
    * Recursively lists a directory while generating relative paths. This is a
    * helper function for findClasses. Note: Uses a StringBuffer to avoid the
    * excessive overhead of multiple String concatentation
    *
    * @param dirListing
    *            A list variable for storing the directory listing as a list of
    *            Strings
    * @param dir
    *            A File for the directory to be listed
    * @param relativePath
    *            A StringBuffer used for building the relative paths
    * @param dirPrefix
    *            A String directory prefix that can be used in the listing of
    *            the directories
    */
   private void recursivelyListDir(List<String> dirListing, File dir, StringBuffer relativePath, String dirPrefix) {
      int prevLen; // used to undo append operations to the StringBuffer

      // if the dir is really a directory
      if (dir.isDirectory()) {
         // get a list of the files in this directory
         File[] files = dir.listFiles();

         // for each file in the present dir
         for (int i = 0; i < files.length; i++) {
            if ((relativePath.length() > 0) && StringUtils.isNotBlank(dirPrefix)) {
               StringBuilder relDir = new StringBuilder(relativePath);
               int replcPeriodPos = relDir.indexOf(".");
               int replcFwdSlshPos = relDir.indexOf("/");
               int replcBwdSlshPos = relDir.indexOf("\\");

               while ((replcPeriodPos > -1) || (replcFwdSlshPos > -1) || (replcBwdSlshPos > -1)) {
                  if (replcPeriodPos > -1) {
                     relDir.replace(replcPeriodPos, replcPeriodPos + 1, "|");
                  } else if (replcFwdSlshPos > -1) {
                     relDir.replace(replcFwdSlshPos, replcFwdSlshPos + 1, "|");
                  } else {
                     relDir.replace(replcBwdSlshPos, replcBwdSlshPos + 1, "|");
                  }

                  replcPeriodPos = relDir.indexOf(".");
                  replcFwdSlshPos = relDir.indexOf("/");
                  replcBwdSlshPos = relDir.indexOf("\\");
               }

               if (relDir.length() < dirPrefix.trim().length()) {
                  if (!dirPrefix.trim().startsWith(relDir.toString())) {
                     break;
                  }
               } else if (relDir.length() > dirPrefix.trim().length()) {
                  if (!relDir.toString().startsWith(dirPrefix.trim())) {
                     break;
                  }
               } else if (!relDir.toString().equals(dirPrefix)) {
                  break;
               }
            }

            // store our original relative path string length
            prevLen = relativePath.length();
            // call this function recursively with file list from present
            // dir and relateveto appended with present dir
            recursivelyListDir(dirListing, files[i], relativePath.append(prevLen == 0 ? "" : File.separator).append(files[i].getName()), dirPrefix);
            // delete subdirectory previously appended to our relative path
            relativePath.delete(prevLen, relativePath.length());
         }
      } else {
         // this dir is a file; append it to the relativeto path and add it
         // to the directory listing
         dirListing.add(relativePath.toString());
      }
   }
}
