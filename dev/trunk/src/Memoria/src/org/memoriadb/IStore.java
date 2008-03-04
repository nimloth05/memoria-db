package org.memoriadb;

import org.memoriadb.core.IObjectInfo;
import org.memoriadb.id.IObjectId;

public interface IStore {
  
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
  
  public boolean containsId(IObjectId id);
  
  /**
   * Commits the changes since the last call to <tt>beginUpdate</tt>. 
   * Updates can be nested, what increases the update-counter. Changes are only written to the
   * persistent store if the update-counter is 0. 
   */
  public void endUpdate();

  /**
   * @return The head revision of this database. Is incremented after each transaction.
   */
  public long getHeadRevision();
  
  public IObjectInfo getObjectInfo(Object object);
  
  /**
   * Clients which need information about the stored java class hierarchy or want to add new memoria
   * classes should work with ghe returned ITypeInfo interface. 
   */
  public ITypeInfo getTypeInfo();
  
  /**
   * @return true, if the update-counter is > 0.  
   */
  public boolean isInUpdateMode();
  
}
