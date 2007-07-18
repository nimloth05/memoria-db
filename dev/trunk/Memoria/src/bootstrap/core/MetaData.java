package bootstrap.core;

import java.io.*;
import java.util.*;

public class MetaData {
  
  private Map<Class<?>, MetaClass> fTypeToMetaInfo = new HashMap<Class<?>, MetaClass>();
  
  public MetaData() {
    MetaClass metaClass = new MetaClass(MetaClass.class);
    add(MetaClass.class, metaClass);
  }
  
  /**
   * Invariant: the highest value of all typeIds.
   */
  private long fCurrentTypeId;

  public MetaClass register(IContext context, DataOutput dataStream, Class<?> type) throws Exception  {
    MetaClass info = fTypeToMetaInfo.get(type);
    if (info == null) {
      info = new MetaClass(++fCurrentTypeId, type);
      add(type, info);
      context.serializeObject(dataStream, info);
    }
    return info;
  }
  
  private void add(Class<?> klass, MetaClass metaClass) {
    fTypeToMetaInfo.put(klass, metaClass);
    fIdToMetaInfo.put(metaClass.getObjectId(), metaClass);
  }
  
  public MetaClass getMetaClass(long typeId) {
    return fIdToMetaInfo.get(typeId);
  }
  
  @Override
  public String toString() {
    return fIdToMetaInfo.toString();
  }

  public void readMetaClass(DataInputStream stream, long objectId) {
    
  }

}
