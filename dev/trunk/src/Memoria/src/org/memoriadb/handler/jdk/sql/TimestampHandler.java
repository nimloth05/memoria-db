package org.memoriadb.handler.jdk.sql;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.jdk.JDKDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

import java.io.DataOutput;
import java.sql.Timestamp;


/**
 * Handler for java.sql.TimestampHandler
 */
public final class TimestampHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!getClassName().equals(className))
      throw new SchemaException("I am a handler for type " + getClassName() + " but I was called for " + className);
  }

  @Override
  public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
    final long time = input.readLong();
    Timestamp timestamp = new Timestamp(time);

    return !context.isInDataMode() ? timestamp : JDKDataObject.create(typeId, timestamp);
  }

  @Override
  public String getClassName() {
    return Timestamp.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    Timestamp timestamp = getObject(obj);
    output.writeLong(timestamp.getTime());
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
  }

  @SuppressWarnings("unchecked")
  private Timestamp getObject(Object obj) {
    if (obj instanceof JDKDataObject<?>) return ((JDKDataObject<Timestamp>) obj).getObject();
    return (Timestamp) obj;
  }

}
