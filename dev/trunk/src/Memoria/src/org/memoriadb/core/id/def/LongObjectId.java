package org.memoriadb.core.id.def;

import java.io.*;

import org.memoriadb.core.id.IObjectId;

public final class LongObjectId implements IObjectId {

  private final long fValue;

  public LongObjectId(long value) {
    fValue = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final LongObjectId other = (LongObjectId) obj;
    return fValue == other.fValue;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (fValue ^ (fValue >>> 32));
    return result;
  }

  @Override
  public void writeTo(DataOutput output) throws IOException {
    output.writeLong(fValue);
  }
}
