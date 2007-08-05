package org.memoriadb.core;

import java.io.*;

public class MemoriaObjectOutputStream extends ObjectOutputStream {

  public MemoriaObjectOutputStream(OutputStream out) throws IOException {
    super(out);
  }

}
