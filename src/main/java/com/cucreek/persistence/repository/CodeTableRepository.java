package com.cucreek.persistence.repository;

import com.cucreek.persistence.dto.CodeTableDTO;

import java.util.List;

public interface CodeTableRepository extends BaseRepository
{

   List<CodeTableDTO> findCodeTableBySearch(Class codeTableClass,
         String codeValueColumn, String codeDescriptionColumn,
         String effectiveDateColumn, String endDateColumn,
         String queryString,
         Boolean includeEndDatedCodes, String sortField, String sortOrder, int... rowStartIdxAndCount);
}
