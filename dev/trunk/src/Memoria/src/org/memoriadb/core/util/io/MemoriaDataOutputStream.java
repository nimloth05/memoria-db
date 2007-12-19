package org.memoriadb.core.util.io;

import java.io.*;

import org.memoriadb.core.util.ByteUtil;

public class MemoriaDataOutputStream extends DataOutputStream {

  public MemoriaDataOutputStream(OutputStream out) {
    super(out);
  }
  
  public void writeUnsignedLong(long l) throws IOException {
    ByteUtil.writeUnsignedLong(l, this);
  }

}
