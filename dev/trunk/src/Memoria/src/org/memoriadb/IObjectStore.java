package org.memoriadb;

import java.util.*;

import org.memoriadb.core.meta.IMetaClass;
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

  // wird später entfernt, msc...
  public void checkSanity();

  public void close();

  // query
  public boolean contains(long id);

  public boolean contains(Object obj);

  /**
   * Commits the changes since the last call to <tt>beginUpdate</tt>. 
   * Updates can be nested, what increases the update-counter. Changes are only written to the
   * persistent store if the update-counter is 0. 
   */
  public void endUpdate();

  public <T> List<T> getAll(Class<T> clazz);

  public <T> List<T> getAll(Class<T> clazz, IFilter<T> filter);

  // wird später ersetzt durch die typenbasierte Queries, msc...
  public Collection<Object> getAllObjects();

  /**
   * @return The MetaClass for the given <tt>obj</tt>.
   * @throws MemoriaException if no MetaClas can be found for the given <tt>obj</tt>.
   */
  public IMetaClass getMetaClass(Object obj);

  /**
   * @return The object or null, if no Object exists for the given id. It is not considered if the object is persistent
   *         or not.
   */
  public Object getObject(long id);
  
  /**
   * @return The objectId of the given object.
   * @throws MemoriaException
   *           If the given object can not be found.
   */
  public long getObjectId(Object obj);
  
  /**
   * @return true, if the update-counter is bigger than 0.  
   */
  public boolean isInUpdateMode();
  
  /**
   * Adds an object to the store or performs an update if the object is already contained.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @return The objectId
   */
  public long save(Object obj);
  
  /**
   * Saves the given <tt>obj</tt> and all referenced objects.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 

   * 
   * @return objectId of the given <tt>obj</tt>
   */
  public long saveAll(Object root);
  
}
