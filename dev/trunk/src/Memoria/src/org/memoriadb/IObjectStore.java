package org.memoriadb;

import java.util.*;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.exception.MemoriaException;

/**
 * Facade to a memoria DB
 * 
 * @author msc
 * 
 */
public interface IObjectStore {

  /**
   * Starts an update. Changes are immediately refelcted in memory, but not written back to the
   * persistent store until <tt>endUpdate()</tt> is called.
   */
  public void beginUpdate();

  public void close();

  // query
  public boolean containsId(IObjectId id);

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
   * Commits the changes since the last call to <tt>beginUpdate</tt>. 
   * Updates can be nested, what increases the update-counter. Changes are only written to the
   * persistent store if the update-counter is 0. 
   */
  public void endUpdate();

  public <T> List<T> getAll(Class<T> clazz);
  
  public <T> List<T> getAll(Class<T> clazz, IFilter<T> filter);

  public List<Object> getAll(String clazz);
  
  public List<Object> getAll(String clazz, IFilter<Object> filter);
  
  // wird später ersetzt durch die typenbasierte Queries, msc...
  //FIXME: Auf eine Test-Schnittstelle verschieben
  public Collection<Object> getAllObjects();

  public IObjectId getArrayMetaClass();
  
  public IObjectId getHandlerMetaClass();

  /**
   * @return The head revision of this database. Is incremented after each transaction.
   */
  public long getHeadRevision();
  
  /**
   * @return The Class for the given <tt>obj</tt> or null.
   */
  public IMemoriaClass getMemoriaClass(Class<?> clazz);

  /**
   * @return The Class for the given <tt>obj</tt> or null.
   */
  public IMemoriaClass getMemoriaClass(Object obj);
 
  public IObjectId getMemoriaFieldMetaClass();
  /**
   * @return The object or null, if no Object exists for the given id. It is not considered if the object is persistent
   *         or not.
   */
  public <T> T getObject(IObjectId id);
  
  
  /**
   * @return The objectId of the given object.
   * @throws MemoriaException
   *           If the given object can not be found.
   */
  public IObjectId getObjectId(Object obj);

  /**
   * @return true, if the update-counter is > 0.  
   */
  public boolean isInUpdateMode();
  
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
   * Adds the given objects to the store or performs an update if the given objects are already contained.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @return The objectIds of the added or updated objects.
   */
  public IObjectId[] save(Object... objs);
  
  /**
   * Saves the given <tt>root</tt> object and all referenced objects.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @return objectId of the given <tt>root</tt> object.
   */
  public IObjectId saveAll(Object root);

  
  /**
   * Saves the given <tt>root</tt> objects and all referenced objects.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @return objectIds of the given <tt>root</tt> objects.
   */
  public IObjectId[] saveAll(Object... roots);
}
