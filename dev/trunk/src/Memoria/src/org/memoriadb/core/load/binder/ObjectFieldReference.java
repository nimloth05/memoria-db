package org.memoriadb.core.load.binder;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.*;
import org.memoriadb.handler.field.IFieldbasedObject;


public class ObjectFieldReference implements IBindable {
  
  private final String fFieldName;
  private final IFieldbasedObject fSource;
  private final IObjectId fTargetObjectId;

  public ObjectFieldReference(IFieldbasedObject source, String fieldName, IObjectId targetObjectId){
    fSource = source;
    fFieldName = fieldName;
    fTargetObjectId = targetObjectId;
  }

  public void bind(IReaderContext context) throws Exception {
    fSource.set(fFieldName, context.getObject(fTargetObjectId));
  }
  
}
