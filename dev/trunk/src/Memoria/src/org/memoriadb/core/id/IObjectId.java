package org.memoriadb.core.id;

import java.io.DataOutput;

public interface IObjectId {

  public void writeTo(DataOutput output);

}
