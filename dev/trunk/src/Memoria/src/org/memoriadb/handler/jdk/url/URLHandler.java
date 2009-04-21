package org.memoriadb.handler.jdk.url;

import java.io.*;
import java.net.URL;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.jdk.JDKDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public class URLHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!getClassName().equals(className)) throw new SchemaException("I am a handler for type " + getClassName() + " but I was called for " + className);
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws Exception {
    String url = input.readUTF();
    URL urlObject = new URL(url); 
    
    return !context.isInDataMode() ? urlObject : JDKDataObject.create(typeId, urlObject);
  }

  @Override
  public String getClassName() {
    return URL.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    URL url = getURLObject(obj);
    output.writeUTF(url.toString());
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {}

  @SuppressWarnings("unchecked")
  private URL getURLObject(Object obj) {
    if (obj instanceof JDKDataObject<?>) return ((JDKDataObject<URL>)obj).getObject();
    return (URL) obj;
  }

}
