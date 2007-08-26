package org.memoriadb.core.handler.def;

import java.io.*;
import java.util.ArrayList;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.load.*;

public class ArrayListHandler implements ISerializeHandler {

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context) throws Exception {
    ArrayList<Object> result = new ArrayList<Object>();
    while (input.available() > 0) {
      long objectId = input.readLong();
      context.objectToBind(new ArrayListBindable(result, objectId));
    }
    return result;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    ArrayList<?> list = (ArrayList<?>) obj;
    for(Object listEntry: list) {
      output.writeLong(context.getObjectId(listEntry));
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    ArrayList<?> list = (ArrayList<?>) obj;
    
    for(Object listEntry: list) {
      traversal.handle(listEntry);
    }
  }

}
