package bootstrap.core;

import java.io.*;
import java.util.*;

public class MetaData {
  
  private Map<Class<?>, MetaClass> fTypeToMetaInfo = new HashMap<Class<?>, MetaClass>();
  
  public void bootstrap(IContext context) {
    MetaClass classObject = new MetaClass(MetaClass.class);
    putInCaches(context, classObject, MetaClass.METACLASS_OBJECT_ID);
  }

  public MetaClass register(IContext context, DataOutput dataStream, Class<?> type) throws Exception  {
    MetaClass info = fTypeToMetaInfo.get(type);
    
    if (info == null) {
      info = new MetaClass(type);
      add(type, info);
      context.serializeObject(dataStream, info);
    }
    return info;
  }
  
  private void add(Class<?> klass, MetaClass metaClass) {
    fTypeToMetaInfo.put(klass, metaClass);
  }
  
  public void readMetaClass(IContext context, DataInputStream stream, long objectId) throws Exception {
    String className = stream.readUTF();
    MetaClass classObject = new MetaClass(className);
    classObject.readMetaFields(stream, classObject);
    putInCaches(context, classObject, objectId);
  }

  public Collection<MetaClass> getMetaClass() {
    return Collections.unmodifiableCollection(fTypeToMetaInfo.values());
  }
  
  private void putInCaches(IContext context, MetaClass classObject, long objectId) {
    context.put(objectId, classObject);
    fTypeToMetaInfo.put(classObject.getJavaClass(), classObject);
  }

}
