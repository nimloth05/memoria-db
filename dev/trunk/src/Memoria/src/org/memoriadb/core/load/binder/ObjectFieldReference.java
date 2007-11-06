package org.memoriadb.core.load.binder;

import java.lang.reflect.Field;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.*;
import org.memoriadb.exception.MemoriaException;


public class ObjectFieldReference implements IBindable {
  
  private final Field fField;
  private final Object fSource;
  private final IObjectId fTargetObjectId;

  public ObjectFieldReference(Object source, Field field, IObjectId targetObjectId){
    fSource = source;
    fField = field;
    fTargetObjectId = targetObjectId;
  }
  
  
  public void bind(IReaderContext context) throws Exception {
    Object target = context.getObjectById(fTargetObjectId);
    if (target == null) throw new MemoriaException("could not bind object: " + fSource + " target: " + fTargetObjectId + " " + fField);
    fField.set(fSource, target);
  }
  
}
