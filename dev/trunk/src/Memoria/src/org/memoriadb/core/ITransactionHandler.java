package org.memoriadb.core;

import java.util.*;

import org.memoriadb.core.block.*;
import org.memoriadb.core.file.Header;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.exception.MemoriaException;

public interface ITransactionHandler {
  /**
   * Adds a MemoriaClass (if not already present) for the given java class. Saves the changes if no in upate-mode
   * 
   * @param clazz
   * @return TODO
   */
  public IObjectId addMemoriaClass(Class<?> clazz);

  /**
   * Starts an update. Changes are immediately refelcted in memory, but not written back to the
   * persistent store until <tt>endUpdate()</tt> is called.
   */
  public void beginUpdate();

  public void checkIndexConsistancy();
  
  /**
   * Closes this ObjectStore permanently. Open FileHandles are also closed. 
   * After calling close() this ObjectStore holds no locks in the FS.
   */
  public void close();

  public boolean contains(Object obj);

  // query
  public boolean containsId(IObjectId id);
  
  /**
   * Removes the given object. Removed objects can later be added again, resulting in a new id.
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

  public Collection<Object> getAllObjects();
  
  public IBlockManager getBlockManager();
  
  
  public IDefaultIdProvider getDefaultIdProvider();

  /**
   * @return The objectId of the given object.
   * @throws MemoriaException
   *           If the given object can not be found.
   */
  public IObjectId getExistingId(Object obj);
  
  public Header getHeader();
  
  /**
   * @return The head revision of this database. Is incremented after each transaction.
   */
  public long getHeadRevision();
  

  /**
   * @return The objectId or null.
   */
  public IObjectId getId(Object obj);
  
  public int getIdSize();
  
  public IObjectId getMemoriaArrayClass();

  /**
   * Works in either data or object mode.
   * 
   * @return IMemoriaClass for the given <tt>object</tt>.
   */
  public IMemoriaClass getMemoriaClass(Object object);
  
  /**
   * @return The MemoriaClass for the given <tt>className</tt> or null.
   */
  public IMemoriaClass getMemoriaClass(String className);
  
  /**
   * Works in either data or object mode.
   * 
   * @return IObjectId of the MemoriaClass for the given <tt>object</tt>.
   */
  public IObjectId getMemoriaClassId(Object object);
  
  /**
   * @return The Class for the given <tt>obj</tt> or null.
   */
  public IObjectId getMemoriaClassId(String className);
  
  /**
   * @return The object or null, if no Object exists for the given id. It is not considered if the object is persistent
   *         or not.
   */
  public <T> T getObject(IObjectId id);

  /**
   * @return The stored ObjectInfo for the given object or null, if the given obj is unknown or deleted.
   */
  public IObjectInfo getObjectInfo(Object obj);
  
  /**
   * @return The stored ObjectInfo for the given id or null, if the given id is unknown. This method may work
   * even for deleted objects, if the delete-marker is still present.
   */
  public IObjectInfo getObjectInfoForId(IObjectId id);

  public Set<ObjectInfo> getSurvivors(Block block);
  
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
