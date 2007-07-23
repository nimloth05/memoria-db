package bootstrap.core;

import java.io.DataOutput;
import java.lang.reflect.Field;

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
  
  public MetaClass getMetaObject(Class<?> javaType);

  public Object getObejctById(long objectId);

  public long register(Object object);


  public void objectToBind(Object object, Field field, long targetId);
  
}
