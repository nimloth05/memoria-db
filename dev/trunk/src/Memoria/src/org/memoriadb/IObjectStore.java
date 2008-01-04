package org.memoriadb;

import java.util.List;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.id.IObjectId;

/**
 * Facade to a memoria DB
 * 
 * @author msc
 * 
 */
public interface IObjectStore extends IStore {


  public boolean contains(Object obj);
  
  /**
   * Removes the given object from this ObjectStore. Removed objects can later be added again, resulting in a new id.
   * 
   * References to the given object remain untouched for the current object graph, but will be null
   * when the file is opened with a new ObjectStore.
   * 
   * If the given object is not contained in this store, nothing will happen.
   * 
   * @param obj Object which is deleted.
   */
  public void delete(Object obj);

  /**
   * Removes the given object-graph from this ObjectStore. 
   *
   * References to the deleted objects remain untouched for the current object graph, but will be null
   * when the file is opened with a new ObjectStore.
   * 
   * If the given object is not contained in this store, nothing will happen.
   * 
   * @param obj
   */
  public void deleteAll(Object root);

  /**
   * @return The object or null, if no Object exists for the given id. It is not considered if the object is persistent
   *         or not.
   */
  public <T> T get(IObjectId id);

  /**
   * 
   * @return all Objects in the Memoria-Store.
   */
  public Iterable<Object> getAllObjects();
  
  /**
   * 
   * @return all Objects in the Memoria-Store <b>except</b> Memoria-Classes.
   */
  public Iterable<Object> getAllUserSpaceObjects();

  /**
   * @return The objectId of the given object.
   * @throws MemoriaException
   *           If the given object can not be found.
   */
  public IObjectId getId(Object obj);
  
  public <T> List<T> query(Class<T> clazz);

  public <FILTER, T extends FILTER> List<T> query(Class<T> clazz, IFilter<FILTER> filter);
  
  public <T> List<T> query(String clazz);
  
  public List<Object> query(String clazz, IFilter<Object> filter);
  
  /**
   * Adds the given object to the store or performs an update if the given object is already contained.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @return The objectId of the added or updated object.
   */
  public IObjectId save(Object obj);
  
  /**
   * Saves the given <tt>root</tt> object and all referenced objects.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @return objectId of the given <tt>root</tt> object.
   */
  public IObjectId saveAll(Object root);

}
