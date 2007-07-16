package bootstrap.core;

import java.io.DataOutput;

public interface Context {
  public void serializeObject(DataOutput dataStream, Object object) throws Exception;
}
