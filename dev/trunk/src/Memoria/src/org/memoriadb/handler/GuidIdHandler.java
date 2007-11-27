package org.memoriadb.handler;

import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.guid.GuidId;
import org.memoriadb.instantiator.IInstantiator;

public class GuidIdHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if(!className.equals(getClassName())) throw new MemoriaException("wrong type in GuidIdHandler: " + className);
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws Exception {
    long most = input.readLong();
    long least = input.readLong();
    return new GuidId(most, least);
  }

  @Override
  public String getClassName() {
    return GuidId.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    GuidId id = (GuidId) obj;
    output.writeLong(id.getMostSignificantBits());
    output.writeLong(id.getLeastSignificantBits());
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
  }

}
