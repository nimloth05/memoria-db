package org.memoriadb.core.file;

import org.memoriadb.core.block.Block;
import org.memoriadb.core.load.HydratedObject;

/**
 * This handler is called during the read-process of the {@link FileReader}
 * 
 * @author msc
 *
 */
public interface IFileReaderHandler {
  public void block(Block block);
  public void header(FileHeader header);
  
  public void metaClass(HydratedObject metaClass, long id, long version);
  public void metaClassDeleted(long id, long version);
  
  public void object(HydratedObject object, long id, long version);
  public void objectDeleted(long id, long version);
  
}
