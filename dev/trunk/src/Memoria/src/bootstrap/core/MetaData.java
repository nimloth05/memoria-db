package bootstrap.core;

import java.io.DataOutput;

public class MetaData {
  
  public MetaClass register(IContext context, DataOutput dataStream, Class<?> type) throws Exception  {
    MetaClass info = context.getMetaObject(type);
    
    if (info == null) {
      info = new MetaClass(type);
      context.serializeObject(dataStream, info);
    }
    return info;
  }
  

}
