package org.memoriadb.id;

import java.io.*;

public interface IObjectIdFactory extends IDefaultIdProvider {
  
  public void adjustId(IObjectId id);

  public IObjectId createFrom(DataInput input) throws IOException;

  public IObjectId createNextId();
  
  /**
   * @return The number of bytes a IObjectId from thsi factory requires. The size must be equal for all ids.
   */
  public int getIdSize();

}
