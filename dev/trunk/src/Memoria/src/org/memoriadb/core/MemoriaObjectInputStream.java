package org.memoriadb.core;

import java.io.*;

public class MemoriaObjectInputStream extends ObjectInputStream {

  protected MemoriaObjectInputStream(InputStream stream) throws IOException, SecurityException {
    super(stream);
  }

}
