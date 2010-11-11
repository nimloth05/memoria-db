/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.id.guid;

import java.io.*;
import java.util.UUID;

import org.memoriadb.core.util.ByteUtil;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.id.IObjectId;

/**
 * @author Sandro
 */
public class GuidId implements IObjectId {

  private final UUID fUUID;

  public static IObjectId fromString(String string) {
    return new GuidId(UUID.fromString(string));
  }

  public static IObjectId random() {
    return new GuidId(UUID.randomUUID());
  }

  public static IObjectId readFrom(IDataInput input) throws IOException {
    return new GuidId(ByteUtil.readUUID(input));
  }

  public GuidId(UUID uuid) {
    fUUID = uuid;
  }

  @Override
  public String asString() {
    return fUUID.toString();
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

  public UUID getUUID() {
    return fUUID;
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
