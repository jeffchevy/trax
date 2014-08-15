package org.trax.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 * JPA based implementation of the Generic Dao
 */
@SuppressWarnings("unchecked")
public class GenericJpaDao<T, Id extends Serializable> implements GenericDao<T, Id>
{
	@PersistenceContext
	protected EntityManager entityManager;
	
	private Class<T> persistantClass;

	public GenericJpaDao()
	{
		this.persistantClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
	}
	
	public Class<T> getPersistantClass() 
	{
		return persistantClass;
	}

	public List<T> findAll()
	{
		return entityManager.createQuery("from " + persistantClass.getName()+ " order by sortOrder"  ).getResultList();
	}
	
	public T findById(Id id, boolean lock)
	{
		T entity;
	     if(lock)
	     {
	    	 entity = (T) entityManager.find(getPersistantClass(), id);
	    	 entityManager.lock(entity, LockModeType.WRITE);
	     }
	     else
	     {
	    	 entity = (T) entityManager.find(getPersistantClass(), id);
	     }
	     return entity;  
	}
	
	public void remove(T entity)
	{
		entityManager.remove(entity);
	}

	public T save(T entity)
	{
		// With merge, hibernate checks if this is an update or an insert and performs the operation
		return entityManager.merge(entity);
	}

	/* Save a new object
	 * (non-Javadoc)
	 * @see org.trax.dao.GenericDao#persist(java.lang.Object)
	 */
	public void persist(T entity)
	{
		entityManager.persist(entity);
	}
	
	public void merge(T entity)
	{
		entityManager.merge(entity);
	}
}
