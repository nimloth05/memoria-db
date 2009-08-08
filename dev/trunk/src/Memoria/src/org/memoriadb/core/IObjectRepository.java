package org.memoriadb.core;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.IObjectIdFactory;

import java.util.Collection;

/**
 * This interfaces encapsulates the indexes to the ObjectInfo. The methods are only intended 
 * for normal use, i.e. after bootstrapping and loading from file.
 * 
 * @author sandro
 *
 */
public interface IObjectRepository {

  /**
   * Adds an object to the container. A new objectId is generated.
   * @pre The given obj is not already in the container.
   * @return The newly generated id.
   */
  public ObjectInfo add(Object obj, IObjectId memoriaClassId);

  
  public void checkIndexConsistency();
  
  public boolean contains(IObjectId id);
  
  public boolean contains(Object obj);
  
  /**
   * Called when an object is deleted in the same transaction as it was added.
   * @param obj object to delete.
   */
  public IObjectInfo delete(Object obj);
  
  public Iterable<IMemoriaClass> getAllClasses();

  public Collection<IObjectInfo> getAllObjectInfo();

  /**
   * @return All objects in the repository, including all bootstrapped or class-objects
   */
  public Iterable<Object> getAllObjects();
  
  public Iterable<Object> getAllUserSpaceObjects();

  public IObjectId getExistingId(Object obj);

  public Object getExistingObject(IObjectId id);
  
  /**
   * @return The objectId of the given object.
   * @throws MemoriaException If the given object can not be found.
   */
  public IObjectId getId(Object obj);

  public IObjectIdFactory getIdFactory();

  /**
   * @return the memoria-class for the given obejct (works NOT for value-objects).
   */
  public IMemoriaClass getMemoriaClass(Object object);
  
  /**
   * @param klass full qualified class name. 
   * @return The MetaClass for the given java-type. Array-Metaclass is the given <tt>klass</tt>
   * is an array.
   * @throws MemoriaException if no MetaClass can be found
   */
  public IMemoriaClassConfig getMemoriaClass(String klass);


  /**
   * @return The object for the given Id even if the transaction is not completed.
   *         <code>null</code> if the object was not found. 
   */
  public Object getObject(IObjectId id);


  /**
   * @return The stored ObjectInfo for the given object or null, if the given obj is unknown or deleted.
   */
  public ObjectInfo getObjectInfo(Object obj);

  /**
   * @return The stored ObjectInfo for the given id or null, if the given id is unknown. This method may work
   * even for deleted objects, if the delete-marker is still present.
   */
  public ObjectInfo getObjectInfoForId(IObjectId id);


  /**
   * @return true, if the given obj is a metaclass
   */
  public boolean isMemoriaClass(Object obj);

}
