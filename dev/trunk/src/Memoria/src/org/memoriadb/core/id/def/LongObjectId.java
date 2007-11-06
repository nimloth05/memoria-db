package org.memoriadb.core.id.def;

import java.io.DataOutput;

import org.memoriadb.core.id.IObjectId;

public final class LongObjectId implements IObjectId {

  private final long fValue;

  public LongObjectId(long value) {
    fValue = value;
  }

  @Override
  public void writeTo(DataOutput output) {
    output.writeLong(fValue);
  }

}
