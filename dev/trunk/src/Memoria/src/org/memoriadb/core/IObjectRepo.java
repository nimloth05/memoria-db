package org.memoriadb.core;

import java.util.Collection;

import org.memoriadb.core.meta.IMetaClass;
import org.memoriadb.core.repo.ObjectInfo;
import org.memoriadb.exception.MemoriaException;

public interface IObjectRepo {

  /**
   * Adds an object to the container. A new objectId is generated.
   * @pre The given obj is not already in the container.
   * @return The newly generated id.
   */
  public long add(Object obj);

  public void checkSanity();
  
  public boolean contains(long id);
  
  public boolean contains(Object obj);
  
  /**
   * @return creates the metaclass for the given <tt>obj</tt>
   */
  public IMetaClass createMetaClass(Class<?> klass);
  
  public Collection<Object> getAllObjects();
  
  /**
   * @return The MetaClass for the given java-type. Array-Metaclass is the given <tt>klass</tt>
   * is an array.
   * @throws MemoriaException if no MetaClass can be found
   */
  public IMetaClass getMetaClass(Class<?> klass);

  /**
   * @return The object or null, if no Object exists for the given id. 
   *         It is not considered if the object is persistent or not.
   */
  public Object getObject(long id);

  /**
   * @return The objectId of the given object.
   * @throws MemoriaException If the given object can not be found.
   */
  public long getObjectId(Object obj);

  /**
   * @return The stored ObjectInfo for the given object or null, if the given <tt>obj</tt> is unknown.
   */
  public ObjectInfo getObjectInfo(Object obj);

  /**
   * @return true, if the metaClass for the given <tt>obj</tt> already exists.
   */
  public boolean metaClassExists(Class<?> klass);

  /**
   * Tells the ObjectContainer that an existing object has been updated.
   * @param obj
   */
  public void update(Object obj);


}
