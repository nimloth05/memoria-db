package org.memoriadb.core;

import java.util.Collection;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.IMemoriaClassConfig;

/**
 * This interfaces encapsulates the indexes to the ObjectInfos. The methods are only intended 
 * for normal use, i.e. after bootstrapping and loading from file.
 * 
 * @author msc
 *
 */
public interface IObjectRepo extends IDefaultIdProvider {

  /**
   * Adds an object to the container. A new objectId is generated.
   * @pre The given obj is not already in the container.
   * @return The newly generated id.
   */
  public ObjectInfo add(Object obj, IObjectId memoriaClassId);
  
  public void checkIndexConsistancy();
  
  public boolean contains(IObjectId id);
  
  public boolean contains(Object obj);
  
  /**
   * Called when an object is deleted in the same transaction as it was added.
   * @param obj Object to delete
   * @throws MemoriaException if the given <tt>obj</tt> is not found.
   */
  public ObjectInfo delete(Object obj);
  
  public Collection<IObjectInfo> getAllObjectInfos();

  public Collection<Object> getAllObjects();

  /**
   * @return The objectId of the given <tt>obj</tt>.
   * @throws MemoriaException if the given <tt>obj</tt> is not found
   */
  public IObjectId getExistingId(Object obj);

  /**
   * @return The object for the given <tt>id</tt>.
   * @throws MemoriaException if no object is found for the given <tt>id</tt>
   */
  public Object getExistingObject(IObjectId id);

  
  /**
   * @return The objectId of the given <tt>obj</tt> or null.
   */
  public IObjectId getId(Object obj);

  public IObjectIdFactory getIdFactory();
  
  /**
   * @return The MetaClass for the given java-type or null.
   * Returns array-class if the given <tt>klass</tt> is an array.
   */
  public IMemoriaClassConfig getMemoriaClass(String klass);

  /**
   * @return The object for the given <tt>id</tt> or null.
   */
  public Object getObject(IObjectId id);

  
  /**
   * @return The stored ObjectInfo for the given <tt>obj</tt> or null.
   */
  public ObjectInfo getObjectInfo(Object obj);

  /**
   * @return The stored ObjectInfo for the given <tt>id</tt> or null. 
   * 
   * May work for deleted objects, if the delete-marker is still present.
   */
  public ObjectInfo getObjectInfoForId(IObjectId id);
  
  /**
   * @return true, if the given obj is a memoria class
   */
  public boolean isMemoriaClass(Object obj);


  /**
   * Tells the ObjectContainer that an object was added to the persistent store.
   */
  public void updateObjectInfoAdded(Object obj, long revision);


  /**
   * Tells the ObjectContainer that a DeleteMarker was written to the persistent store for the given id. 
   */
  public void updateObjectInfoDeleted(IObjectId id, long headRevision);

  /**
   * Tells the ObjectContainer that an existing object has been updated, i.e. a new generation was
   * written to the persistent store.
   */
  public void updateObjectInfoUpdated(Object obj, long headRevision);

}
