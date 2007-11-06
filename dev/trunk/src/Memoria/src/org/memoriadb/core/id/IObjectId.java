package org.memoriadb.core.id;

import java.io.*;

public interface IObjectId {

  public void writeTo(DataOutput output) throws IOException;

}
