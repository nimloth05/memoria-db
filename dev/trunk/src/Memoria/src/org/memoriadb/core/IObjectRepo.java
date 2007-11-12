package org.memoriadb.core;

import java.util.Collection;

import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.exception.MemoriaException;

/**
 * This interfaces encapsulates the indexes to the ObjectInfos. The methods are only intended 
 * for normal use, i.e. after bootstrapping and loading from file.
 * 
 * @author msc
 *
 */
public interface IObjectRepo extends IDefaultObjectIdProvider {

  /**
   * Adds an object to the container. A new objectId is generated.
   * @pre The given obj is not already in the container.
   * @return The newly generated id.
   */
  public IObjectId add(Object obj, IObjectId memoriaClassId);

  
  public void checkSanity();
  
  public boolean contains(IObjectId id);
  
  public boolean contains(Object obj);
  
  /**
   * Called when an object is deleted in the same transaction as it was added.
   * @param id
   */
  public ObjectInfo delete(Object obj);
  
  public Collection<IObjectInfo> getAllObjectInfos();

  public Collection<Object> getAllObjects();

  public IObjectIdFactory getIdFactory();

  /**
   * @return The MetaClass for the given java-type. Array-Metaclass is the given <tt>klass</tt>
   * is an array.
   * @throws MemoriaException if no MetaClass can be found
   */
  public IMemoriaClassConfig getMemoriaClass(String klass);

  /**
   * @return The object or null, if no Object exists for the given id. 
   *         It is not considered if the object is persistent or not.
   */
  public Object getObject(IObjectId id);
  
  /**
   * @return The objectId of the given object.
   * @throws MemoriaException If the given object can not be found.
   */
  public IObjectId getObjectId(Object obj);

  /**
   * @return The stored ObjectInfo for the given id or null, if the given id is unknown. This method may work
   * even for deleted objects, if the delete-marker is still present.
   */
  public IObjectInfo getObjectInfo(IObjectId id);

  /**
   * @return The stored ObjectInfo for the given object or null, if the given obj is unknown or deleted.
   */
  public IObjectInfo getObjectInfo(Object obj);
  
  /**
   * @return true, if the given obj is a metaclass
   */
  //FIXME: METACLASS
  public boolean isMetaClass(Object obj);


  /**
   * @return true, if the metaClass for the given <tt>obj</tt> already exists.
   */
  //FIXME: METACLASS
  public boolean metaClassExists(Class<?> klass);
  
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
