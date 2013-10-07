package com.cucreek.common;

import com.cucreek.persistence.dto.CodeTableDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ClassFinder utility to get all DFS code tables (annotated with @DfsCodeTable)
 *
 * @author jljdavidson
 *
 */
public class CodeTableEntityFinder extends ClassFinder {
   private final static String DFS_PERSISTENCE_PACKAGE = "gov.mt.mdt.dfs.persistence.entity";

   public List<CodeTableDTO> getCodeTableList() {
      List<CodeTableDTO> retList = new ArrayList<CodeTableDTO>();

      Map<String, Set<Class<?>>> classMap = findClassesWithPackagePrefix(this.getClass().getClassLoader(),
            DFS_PERSISTENCE_PACKAGE, null, null);

      if (classMap != null) {
         for (String keySet : classMap.keySet()) {
            Set<Class<?>> classSet = classMap.get(keySet);
            for (Class<?> clazz : classSet) {
               if (clazz.isAnnotationPresent(CodeTableEntityAnnotation.class)) {
                  CodeTableDTO dto = new CodeTableDTO();
                  dto.setCodeValue(clazz.getSimpleName());
                  dto.setDescription(SystemConstants.splitCamelCase(
                        clazz.getSimpleName()));
                  retList.add(dto);
               }
            }
         }
      }
      Collections.sort(retList,new Comparator<CodeTableDTO>() {
         @Override
         public int compare(CodeTableDTO o1, CodeTableDTO o2) {
            return o1.getDescription().compareTo(o2.getDescription());
         }
      });
      return retList;
   }

   @Override
   protected boolean classMatchesCriteria(Class<?> clss) {
      return false;
   }
}
