package org.memoriadb.handler.enu;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.*;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public class EnumHandler implements IHandler {

  private final String fName;

  public EnumHandler(Class<?> javaClass) {
    this(javaClass.getName());
  }

  public EnumHandler(String name) {
    fName = name;
  }

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {}

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws Exception {
    IEnumObject enumObject = createEnumObject(context, typeId);
    enumObject.setOrdinal(input.readInt());
    return enumObject.getObject((IMemoriaClass) context.getExistingObject(typeId));
  }

  @Override
  public String getClassName() {
    return fName;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    IEnumObject enumObject = createEnumObject(obj);
    output.writeInt(enumObject.getOrdinal());
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {}
  
  private IEnumObject createEnumObject(IReaderContext context, IObjectId memoriaClassId) {
    if (context.isInDataMode()) return new EnumDataObject(memoriaClassId);
    return new EnumObject(memoriaClassId);
  }

  private IEnumObject createEnumObject(Object obj) {
    if (obj instanceof IEnumObject) return (IEnumObject) obj;
    return new EnumObject((Enum<?>) obj);
  }

}
