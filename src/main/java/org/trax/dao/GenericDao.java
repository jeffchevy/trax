package org.trax.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Generic interface for performing CRUD operations
 */
public interface GenericDao<T, Id extends Serializable> 
{
	public T findById(Id id, boolean lock);
	
	public List<T> findAll();
	
	// This can be used to perform an insert or an update
	public T save(T entity);

	public void persist(T entity);
	public void merge(T entity);
	public void remove(T entity);
	
}
