package bootstrap.core;

import java.io.*;

public class MetaData {
  
  public void bootstrap(IContext context) {
    MetaClass classObject = new MetaClass(MetaClass.class);
    context.put(MetaClass.METACLASS_OBJECT_ID, classObject);
  }

  public MetaClass register(IContext context, DataOutput dataStream, Class<?> type) throws Exception  {
    MetaClass info = context.getMetaObject(type);
    
    if (info == null) {
      info = new MetaClass(type);
      context.serializeObject(dataStream, info);
    }
    return info;
  }
  
  public void readMetaClass(IContext context, DataInputStream stream, long objectId) throws Exception {
    String className = stream.readUTF();
    MetaClass classObject = new MetaClass(className);
    classObject.readMetaFields(stream);
    context.put(objectId, classObject);
  }
}
