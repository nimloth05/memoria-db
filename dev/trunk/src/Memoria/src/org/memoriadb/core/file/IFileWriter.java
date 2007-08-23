package org.memoriadb.core.file;

import org.memoriadb.util.IdentityHashSet;

public interface IFileWriter {

  /**
   * Saves the given changes to the persistent store.
   * @param add added or updated objects. MetaClasses are also contained in this list.
   */
  public void write(IdentityHashSet<Object> objects);

}
