package org.memoriadb.example;

import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.*;
import org.memoriadb.handler.field.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public class PersonHandler implements IHandler {
  
  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {}

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws Exception {
    IFieldbasedObject object = creeateObject(context, typeId);
    object.set(Person.NAME_FIELD, input.readUTF());
    return object.getObject();
  }

  @Override
  public String getClassName() {
    return Person.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    IFieldbasedObject dataObject = createDataObject(obj);
    output.writeUTF(dataObject.get(Person.NAME_FIELD).toString());
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    
  }

  private IFieldbasedObject createDataObject(Object obj) {
    if (obj instanceof IDataObject) return (IFieldbasedObject) obj;
    return new FieldbasedObject(obj);
  }

  private IFieldbasedObject creeateObject(IReaderContext context, IObjectId typeId) {
    if (context.isInDataMode()) {
      return new FieldbasedDataObject(typeId);
    }
    return new FieldbasedObject(context.getDefaultInstantiator().newInstance(getClassName()));
  }

}
