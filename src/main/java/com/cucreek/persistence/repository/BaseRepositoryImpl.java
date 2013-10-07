/**
 * 
 */
package com.cucreek.persistence.repository;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * The base repository defines the CRUD methods used by CuCreek Spring repository
 * implementations.  This class sets up the persistence context and entity
 * manager.
 * 
 * @author jljdavidson
 *
 */
public abstract class BaseRepositoryImpl implements BaseRepository
{

   protected Logger logger = Logger.getLogger(BaseRepositoryImpl.class);

   @PersistenceContext
   private EntityManager em;

   @Override
   public EntityManager entityManager()
   {
      return em;
   }

   /* (non-Javadoc)
    * @see gov.mt.mdt.dfs.persistence.repository.BaseRepository#save(java.lang.Object)
    */
   @Override
   public void save(Object entity)
   {
      entityManager().persist(entity);
   }

   /* (non-Javadoc)
    * @see gov.mt.mdt.dfs.persistence.repository.BaseRepository#update(java.lang.Class, java.lang.Object)
    */
   @Override public <T extends Object> T update(Class<T> clazz, Object entity)
   {
      return clazz.cast(entityManager().merge(entity));      
   }
   
   /* (non-Javadoc)
    * @see gov.mt.mdt.dfs.persistence.repository.BaseRepository#delete(java.lang.Object, java.lang.Object)
    */
   @Override
   public void delete(Object entity, Object refId)
   {
      try
      {
         entity = entityManager().getReference(entity.getClass(), refId);                                                                           
         if (entity != null)
         {
            entityManager().remove(entity);
            logger.debug("delete successful");
         }
      }
      catch (RuntimeException re)
      {
         logger.error("delete failed", re);
         throw re;
      }
   }

   
   /* (non-Javadoc)
    * @see gov.mt.mdt.dfs.persistence.repository.BaseRepository#findById(java.lang.Class, java.lang.Object)
    */
   @Override   
   public <T extends Object> T findById(Class<T> entityClass, Object id)
   {
      if (id == null)
         return null;
      try
      {
         Object instance = entityManager().find(entityClass, id);
         return entityClass.cast(instance);
      }
      catch (RuntimeException re)
      {
         logger.error("find failed", re);
         throw re;
      }
   }

   /* (non-Javadoc)
    * @see gov.mt.mdt.dfs.persistence.repository.BaseRepository#findByProperty(java.lang.Class, java.lang.String, java.lang.Object, int[])
    */
   @Override
   public List<?> findByProperty(Class<?> entityClass, String propertyName,
         final Object value, final int... rowStartIdxAndCount)
   {
      try
      {
         final String queryString = "select model from "
               + entityClass.getSimpleName() + " model where model."
               + propertyName + "= :propertyValue";
         Query query = entityManager().createQuery(queryString);
         query.setParameter("propertyValue", value);
         setQueryRowCountParams(query, rowStartIdxAndCount);
         return query.getResultList();

      }
      catch (RuntimeException re)
      {
         logger.error("find by property name failed", re);
         throw re;
      }
   }

   /**
    * Find the first entity that matches a property type (ala a column value).  Think of this
    * as a select first object query.
    */
   @Override
   public <T extends Object> T  findFirstByProperty(Class<T> entityClass, String propertyName,final Object value)
   {
      try
      {
         final String queryString = "select model from "
               + entityClass.getSimpleName() + " model where model."
               + propertyName + "= :propertyValue";
         TypedQuery<T> query = entityManager().createQuery(queryString,entityClass);
         query.setParameter("propertyValue", value);
         List<T> retList = query.getResultList();
         if (retList==null || retList.isEmpty()) return null;
         return retList.get(0);

      }
      catch (RuntimeException re)
      {
         logger.error("find by property name failed", re);
         throw re;
      }
   }

   /**
    * Find all Object entities.
    * 
    * @param rowStartIdxAndCount
    *           Optional int varargs. rowStartIdxAndCount[0] specifies the the
    *           row index in the query result-set to begin collecting the
    *           results. rowStartIdxAndCount[1] specifies the the maximum count
    *           of results to return.
    * @return List<Object> all Object entities
    */
   @Override
   @SuppressWarnings("unchecked")
   public <T> List<T> findAll(Class<T> entityClass,
         final int... rowStartIdxAndCount)
   {
      try
      {
         final String queryString = "select model from "
               + entityClass.getSimpleName() + " model";
         Query query = entityManager().createQuery(queryString);
         setQueryRowCountParams(query, rowStartIdxAndCount);
         return query.getResultList();

      }
      catch (RuntimeException re)
      {
         logger.error("find all failed", re);
         throw re;
      }
   }

   /**
    * Given a JPA query object set the row start index and row count index
    * 
    * @param query
    * @param rowStartIdxAndCount
    */
   public void setQueryRowCountParams(Query query, final int... rowStartIdxAndCount)
   {
      if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0)
      {
         int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
         if (rowStartIdx > 0)
         {
            query.setFirstResult(rowStartIdx);
         }

         if (rowStartIdxAndCount.length > 1)
         {
            int rowCount = Math.max(0, rowStartIdxAndCount[1]);
            if (rowCount > 0)
            {
               query.setMaxResults(rowCount);
            }
         }
      }
   }

}
