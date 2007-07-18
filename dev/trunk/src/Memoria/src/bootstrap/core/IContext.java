package bootstrap.core;

import java.io.DataOutput;

import bootstrap.exception.MemoriaException;

public interface IContext {
  
  public void serializeObject(DataOutput dataStream, Object object) throws Exception;
  

  /**
   * 
   * @param obj
   * @throws MemoriaException if the object was not found.
   * @return
   */
  public long getObjectId(Object obj);
  
  public void put(long objectId, Object obj);
  
}
