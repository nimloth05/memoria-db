package org.memoriadb.core.facade;

import java.util.Collection;

import org.memoriadb.core.IMetaClass;
import org.memoriadb.core.backend.IMemoriaFile;
import org.memoriadb.exception.MemoriaException;

/**
 * Facade to a memoria DB
 * 
 * @author msc
 * 
 */
public interface IMemoria {

  // wird später entfernt, msc...
  public void checkSanity();

  // query
  public boolean contains(long id);

  public boolean contains(Object obj);

  // wird später ersetzt durch die typenasierte Queries, msc...
  public Collection<Object> getAllObjects();

  public IMemoriaFile getFile();

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
   * Adds an object to the store or performs an update if the object is already contained.
   * 
   * @return The objectId
   */
  public long save(Object obj);

  /**
   * Saves the given <tt>obj</tt> and all referenced objects.
   * @return objectId of the given <tt>obj</tt>
   */
  public long saveAll(Object root);

  /**
   * Writes all pending changes since the last call to <tt>writePendingChanges()</tt>
   */
  public void writePendingChanges();

}
