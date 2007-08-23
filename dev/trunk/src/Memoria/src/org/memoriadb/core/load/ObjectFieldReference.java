package org.memoriadb.core.load;

import java.lang.reflect.Field;

import org.memoriadb.exception.MemoriaException;


public class ObjectFieldReference implements IBindable {
  
  private final Field fField;
  private final Object fSource;
  private final long fTargetObjectId;

  public ObjectFieldReference(Object source, Field field, long targetObjectId){
    fSource = source;
    fField = field;
    fTargetObjectId = targetObjectId;
  }
  
  
  public void bind(IReaderContext contex) throws Exception {
    Object target = contex.getObjectById(fTargetObjectId);
    if (target == null) throw new MemoriaException("could not bind object: " + fSource + " target: " + fTargetObjectId + " " + fField);
    fField.set(fSource, target);
  }
  
}
