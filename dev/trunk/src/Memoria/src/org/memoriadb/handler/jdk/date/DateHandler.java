package org.memoriadb.handler.jdk.date;

import java.io.DataOutput;
import java.util.Date;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.jdk.JDKDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public class DateHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!getClassName().equals(className)) throw new SchemaException("I am a handler for type " + getClassName() + " but I was called for " + className);    
  }

  @Override
  public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
    final long time = input.readLong();
    Date date = new Date(time);
    
    return !context.isInDataMode() ? date : JDKDataObject.create(typeId, date);
  }

  @Override
  public String getClassName() {
    return Date.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    Date date = getURLObject(obj);
    output.writeLong(date.getTime());    
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {}
  
  @SuppressWarnings("unchecked")
  private Date getURLObject(Object obj) {
    if (obj instanceof JDKDataObject<?>) return ((JDKDataObject<Date>)obj).getObject();
    return (Date) obj;
  }

}
