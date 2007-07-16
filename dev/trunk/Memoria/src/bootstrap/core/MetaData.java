package bootstrap.core;

import java.io.DataOutput;
import java.util.*;

public class MetaData {
  
  private Map<Class<?>, MetaClass> fTypeToMetaInfo = new HashMap<Class<?>, MetaClass>();
  private Map<Long, MetaClass> fIdToMetaInfo = new HashMap<Long, MetaClass>();
  
  public MetaData() {
    MetaClass metaClass = new MetaClass(1, MetaClass.class);
    add(MetaClass.class, metaClass);

    MetaClass metaField = new MetaClass(2, MetaField.class);
    add(MetaField.class, metaField);
  
    fCurrentTypeId = 2;
  }
  
  /**
   * Invariant: the highest value of all typeIds.
   */
  private long fCurrentTypeId;

  public MetaClass register(Context context, DataOutput dataStream, Class<?> type) throws Exception  {
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
    fIdToMetaInfo.put(metaClass.getTypeId(), metaClass);
  }
  
  public MetaClass getMetaClass(long typeId) {
    return fIdToMetaInfo.get(typeId);
  }
  
  public String toString() {
    return fIdToMetaInfo.toString();
  }

}
