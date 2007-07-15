package bootstrap.core;

import java.util.*;

public class MetaData {
  
  private Map<Class<?>, MetaClass> fTypeToMetaInfo = new HashMap<Class<?>, MetaClass>();
  private Map<Long, MetaClass> fIdToMetaInfo = new HashMap<Long, MetaClass>();
  
  public MetaData() {
    MetaClass metaClass = MetaClass.createOwnMetaInfo();
    
    fTypeToMetaInfo.put(metaClass.getClass(), metaClass);
    fIdToMetaInfo.put(metaClass.getTypeId(), metaClass);
    fCurrentTypeId = metaClass.getTypeId();
  }
  
  
  /**
   * Invariant: the highest value of all typeIds.
   */
  private long fCurrentTypeId;

  public MetaClass register(Class<?> type) {
    MetaClass info = fTypeToMetaInfo.get(type);
    if (info == null) {
      info = MetaClass.createNewInfo(++fCurrentTypeId);
      info.setClassName(type.getName());
      
      fTypeToMetaInfo.put(type, info);
      fIdToMetaInfo.put(info.getTypeId(), info);
    }
    return info;
  }

}
