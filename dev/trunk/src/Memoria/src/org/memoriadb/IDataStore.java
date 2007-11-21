package org.memoriadb;

import java.util.*;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.exception.MemoriaException;

/**
 * Facade to a memoria DB
 * 
 * @author msc
 * 
 */
public interface IDataStore {

  /**
   * Starts an update. Changes are immediately refelcted in memory, but not written back to the
   * persistent store until <tt>endUpdate()</tt> is called.
   */
  public void beginUpdate();

  /**
   * Closes this ObjectStore permanently. Open FileHandles are also closed. 
   * After calling close() this ObjectStore holds no locks in the FS.
   */
  public void close();

  public boolean contains(Object obj);

  // query
  public boolean containsId(IObjectId id);

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

  /**
   * @return The head revision of this database. Is incremented after each transaction.
   */
  public long getHeadRevision();
  
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
   * Saves the given <tt>root</tt> object and all referenced objects.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @return objectId of the given <tt>root</tt> object.
   */
  public IObjectId saveAll(Object root);

}
