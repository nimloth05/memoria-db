package org.memoriadb.core.load.binder;

import org.memoriadb.core.handler.def.field.IFieldObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.*;
import org.memoriadb.exception.MemoriaException;


public class ObjectFieldReference implements IBindable {
  
  private final String fFieldName;
  private final IFieldObject fSource;
  private final IObjectId fTargetObjectId;

  public ObjectFieldReference(IFieldObject source, String fieldName, IObjectId targetObjectId){
    fSource = source;
    fFieldName = fieldName;
    fTargetObjectId = targetObjectId;
  }
  
  
  public void bind(IReaderContext context) throws Exception {
    Object target = context.getObjectById(fTargetObjectId);
    if (target == null) throw new MemoriaException("could not bind object: " + fSource + " target: " + fTargetObjectId + " " + fFieldName);
    fSource.set(fFieldName, target);
  }
  
}
