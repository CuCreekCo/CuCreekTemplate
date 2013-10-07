package com.cucreek.persistence.repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * The base repository defines the CRUD methods used by CuCreek Spring repository
 * implementations.
 * 
 * @author jljdavidson
 *
 */
public interface BaseRepository
{
   public EntityManager entityManager();
   
   /**
    * Perform an initial save of a previously unsaved Object entity. All
    * subsequent persist actions of this entity should use the #update() method.
    * This operation must be performed within the a database transaction context
    * for the entity's data to be permanently saved to the persistence store,
    * i.e., database. This method uses the
    * {@link javax.persistence.EntityManager#persist(Object)
    * EntityManager#persist} operation.
    * 
    * <pre>
    * entityManager.beginTransaction();
    * AddressTypeEntityManager.save(entity);
    * entityManager.commit();
    * </pre>
    * 
    * @param entity
    *           Object entity to persist
    * @throws RuntimeException
    *            when the operation fails
    */
   public void save(Object entity);

   /**
    * Delete a persistent Object entity. This operation must be performed within
    * the a database transaction context for the entity's data to be permanently
    * deleted from the persistence store, i.e., database. This method uses the
    * {@link javax.persistence.EntityManager#remove(Object)
    * EntityManager#delete} operation.
    * 
    * <pre>
    * entityManager.beginTransaction();
    * AddressTypeEntityManager.delete(entity);
    * entityManager.commit();
    * entity = null;
    * </pre>
    * 
    * @param entity
    *           Object entity to delete
    * @throws RuntimeException
    *            when the operation fails
    */
   public void delete(Object entity, Object refId);

   public <T extends Object> T  findById(Class<T> entityClass, Object id);

   /**
    * Find all Object entities with a specific property value.
    * 
    * @param propertyName
    *           the name of the Object property to query
    * @param value
    *           the property value to match
    * @param rowStartIdxAndCount
    *           Optional int varargs. rowStartIdxAndCount[0] specifies the the
    *           row index in the query result-set to begin collecting the
    *           results. rowStartIdxAndCount[1] specifies the the maximum number
    *           of results to return.
    * @return List<AddressType> found by query
    */
   public abstract List<?> findByProperty(Class<?> entityClass,
         String propertyName, final Object value,
         final int... rowStartIdxAndCount);

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
   public <T> List<T> findAll(Class<T> entityClass,
         final int... rowStartIdxAndCount);

   public <T extends Object> T findFirstByProperty(Class<T> entityClass, String propertyName, Object value);

   public <T extends Object> T update(Class<T> clazz, Object entity);

}