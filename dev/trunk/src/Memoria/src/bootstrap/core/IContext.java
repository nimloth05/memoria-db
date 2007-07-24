package bootstrap.core;

import java.io.DataOutput;
import java.lang.reflect.Field;

import bootstrap.exception.MemoriaException;

public interface IContext {
  
  public boolean contains(Object referencee);
  

  public MetaClass getMetaObject(Class<?> javaType);
  
  public Object getObejctById(long objectId);
  
  /**
   * 
   * @param obj
   * @throws MemoriaException if the object was not found.
   * @return
   */
  public long getObjectId(Object obj);

  public void objectToBind(Object object, Field field, long targetId);

  public void put(long objectId, Object obj);


  public long register(Object object);

  public void serializeIfNotContained(Object referencee) throws Exception;


  public void serializeObject(DataOutput dataStream, Object object) throws Exception;
  
}
