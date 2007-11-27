package org.memoriadb.core.id.guid;

import java.io.*;
import java.util.UUID;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.util.ByteUtil;

public class GuidId implements IObjectId {

  private final UUID fUUID;
  
  public static IObjectId random() {
    return new GuidId(UUID.randomUUID());
  }

  public static GuidId readFrom(DataInput input) throws IOException {
    return new GuidId(ByteUtil.readUUID(input));
  }
  
  public GuidId(String string) {
    this(UUID.fromString(string));
  }
  
  private GuidId(UUID uuid) {
    fUUID = uuid;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final GuidId other = (GuidId) obj;
    if (fUUID == null) {
      if (other.fUUID != null) return false;
    }
    else if (!fUUID.equals(other.fUUID)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fUUID == null) ? 0 : fUUID.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return fUUID.toString();
  }

  @Override
  public void writeTo(DataOutput output) throws IOException {
    ByteUtil.writeUUID(output, fUUID);
  }
  
}