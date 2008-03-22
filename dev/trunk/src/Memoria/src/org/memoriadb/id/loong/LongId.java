package org.memoriadb.id.loong;

import java.io.*;

import org.memoriadb.id.*;

public final class LongId implements IObjectId, IIntegralId {

  private final long fValue;

  public LongId(long value) {
    fValue = value;
  }

  @Override
  public String asString() {
    return Long.toString(fValue);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final LongId other = (LongId) obj;
    return fValue == other.fValue;
  }

  /**
   * @deprecated use {@link IIntegralId#getValue()) now.
   * @return
   */
  @Deprecated
  public long getLong() {
    return fValue;
  }
  
  @Override
  public long getValue() {
    return fValue;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (fValue ^ (fValue >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return asString();
  }

  @Override
  public void writeTo(DataOutput output) throws IOException {
    output.writeLong(fValue);
  }

}
