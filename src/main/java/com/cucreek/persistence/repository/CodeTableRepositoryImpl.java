package com.cucreek.persistence.repository;

import com.cucreek.common.CodeTableEntityAnnotation;
import com.cucreek.persistence.dto.CodeTableDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * The CodeTableRepository is the Spring repository (JPA data access object, if you
 * will) that handles code table functions. Thus, it includes queries for
 * getting code table information.
 * 
 * @author jljdavidson
 *
 */
@Repository
@Transactional(readOnly = true)
public class CodeTableRepositoryImpl extends BaseRepositoryImpl implements CodeTableRepository
{

   /**
    * Given a code table class name and it's code table columns, find all matches, partials, and
    * wild card searched code value and description.  This method is typically used for partial
    * searching in an autocomplete.
    *
    * @param codeTableClass JPA entity class for code table annotated with CodeTableEntityAnnotation
    * @param codeValueColumn the column name for the code value in the entity
    * @param codeDescriptionColumn description column name
    * @param effectiveDateColumn effective date column name
    * @param endDateColumn end date column name
    * @param queryString string to query by
    * @param includeEndDatedCodes include codes that have an end date?
    * @param sortField what to sort the list by
    * @param sortOrder how to sort the list
    * @param rowStartIdxAndCount  @return list of code table dto
    */
   @Override
   public List<CodeTableDTO> findCodeTableBySearch(Class codeTableClass,
         String codeValueColumn, String codeDescriptionColumn,
         String effectiveDateColumn, String endDateColumn,
         String queryString,
         Boolean includeEndDatedCodes, String sortField, String sortOrder, int... rowStartIdxAndCount)
   {
      String codeTableClassName = codeTableClass.getSimpleName();

      StringBuilder jpql = new StringBuilder(" select new ").
         append("  com.cucreek.persistence.dto.CodeTableDTO(ctc.").append(codeValueColumn)
            .append(", ").
         append("  ctc.").append(codeDescriptionColumn).append(", ").
         append("  ctc.").append(effectiveDateColumn);

         if(StringUtils.isNotEmpty(endDateColumn)){
            jpql.append(", ").append("  ctc.").append(endDateColumn).append(") from ");
         } else {
            jpql.append(") from ");
         }
         jpql.append(codeTableClassName).append(" ctc ");

      if (StringUtils.isNotEmpty(queryString)) {

         jpql.append(" where (UPPER(ctc.").append(codeValueColumn).append(") = :search0 ").
              append("    or UPPER(ctc.").append(codeValueColumn).append(") like :search1 ").
              append("    or UPPER(ctc.").append(codeValueColumn).append(") like :search2 ").
              append("    or UPPER(ctc.").append(codeDescriptionColumn).append(") like :search3 ").
              append("    or UPPER(ctc.").append(codeDescriptionColumn).append(") like :search4 ").
              append("    or UPPER(ctc.").append(codeDescriptionColumn).append(") like :search5 ").
              append("    or UPPER(ctc.").append(codeDescriptionColumn).append(") = :search6) ");
      }

      if (StringUtils.isNotEmpty(endDateColumn) && !includeEndDatedCodes) {
         if (StringUtils.isEmpty(queryString)) {
            jpql.append("   where ctc.").append(endDateColumn).append(" is null ");
         } else {
            jpql.append("   and ctc.").append(endDateColumn).append(" is null ");
         }
      }
      if(StringUtils.isNotEmpty(sortField)){
         CodeTableEntityAnnotation CodeTableEntityAnnotation = (CodeTableEntityAnnotation)codeTableClass.getAnnotation(CodeTableEntityAnnotation.class);
         String entitySortField;
         if(StringUtils.equalsIgnoreCase("description",sortField)){
            entitySortField = CodeTableEntityAnnotation.description();
         } else if (StringUtils.equalsIgnoreCase("codeValue",sortField)){
            entitySortField = CodeTableEntityAnnotation.code();
         } else if (StringUtils.equalsIgnoreCase("effectiveDate",sortField)){
            entitySortField = CodeTableEntityAnnotation.effectiveDate();
         } else {
            entitySortField = sortField;
         }
         jpql.append(" order by ctc.").append(entitySortField).append(" ").append(sortOrder);
      } else {
         jpql.append(" order by ctc.").append(codeDescriptionColumn).append(" asc ");
      }

      String postfixWildcard = StringUtils.upperCase(queryString + "%");
      String noWildcard = StringUtils.upperCase(queryString);
      String preAndPostfixWildcard = StringUtils.upperCase("%"+queryString+"%");
      String prefixWildcard = StringUtils.upperCase("%"+queryString);

      TypedQuery<CodeTableDTO> query = entityManager().createQuery(jpql.toString(),
            CodeTableDTO.class);
      if (rowStartIdxAndCount != null && rowStartIdxAndCount.length == 2) {
         query.setFirstResult(rowStartIdxAndCount[0]);
         query.setMaxResults(rowStartIdxAndCount[1]);
      }
      if (StringUtils.isNotEmpty(queryString)) {
         query.setParameter("search0",noWildcard);
         query.setParameter("search1",postfixWildcard);
         query.setParameter("search2",prefixWildcard);
         query.setParameter("search3",postfixWildcard);
         query.setParameter("search4",prefixWildcard);
         query.setParameter("search5",preAndPostfixWildcard);
         query.setParameter("search6",noWildcard);
      }

      return query.getResultList();
   }
}
