package org.memoriadb.handler.field;

import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.handler.IBindable;
import org.memoriadb.id.IObjectId;


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
