package com.cucreek.persistence.repository;

import com.cucreek.persistence.dto.HelloDTO;

import java.util.List;

/**
 * Sample Hello repository (data access layer).
 *
 * @author jljdavidson
 *
 */
public interface HelloRepository extends BaseRepository {
   List<HelloDTO> findAllHellos();
}
