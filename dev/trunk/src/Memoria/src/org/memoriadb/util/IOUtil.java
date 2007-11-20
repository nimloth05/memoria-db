package org.memoriadb.util;

import java.io.*;

import org.memoriadb.exception.MemoriaException;

public final class IOUtil {

  public static void close(Closeable closeable) {
    if (closeable == null) return;

    try {
      closeable.close();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }
  
  private IOUtil() {}

}
