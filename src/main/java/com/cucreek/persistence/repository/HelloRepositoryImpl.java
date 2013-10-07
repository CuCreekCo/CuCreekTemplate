package com.cucreek.persistence.repository;

import com.cucreek.persistence.dto.HelloDTO;
import com.cucreek.persistence.entity.HelloEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello repository implementation
 *
 * @author jljdavidson
 */
@Repository
@Transactional(readOnly = true)
public class HelloRepositoryImpl extends BaseRepositoryImpl implements HelloRepository {

   @Override
   public List<HelloDTO> findAllHellos(){
      String jpql = "select h from HelloEntity h ";
      TypedQuery<HelloEntity> query = entityManager().createQuery(jpql, HelloEntity.class);

      List<HelloDTO> returnList = new ArrayList<HelloDTO>();
      for (HelloEntity helloEntity : query.getResultList()) {
         returnList.add(new HelloDTO(helloEntity));
      }
      return returnList;
   }
}
