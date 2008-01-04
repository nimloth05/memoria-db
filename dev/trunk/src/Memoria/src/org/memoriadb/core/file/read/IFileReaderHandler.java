package org.memoriadb.core.file.read;

import org.memoriadb.block.Block;
import org.memoriadb.id.IObjectId;

/**
 *
 * This handler is called during the read-process of the {@link FileReader}
 * 
 * @author msc
 *
 */
public interface IFileReaderHandler {
  
  public void block(Block block);

  public void memoriaClass(HydratedObject metaClass, IObjectId id, long version, int size);
  public void memoriaClassDeleted(IObjectId id, long version);
  
  public void object(HydratedObject object, IObjectId id, long version, int size);
  public void objectDeleted(IObjectId id, long version);
  
}
