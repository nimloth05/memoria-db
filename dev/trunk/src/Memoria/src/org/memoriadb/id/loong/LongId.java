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

package org.memoriadb.id.loong;

import java.io.*;

import org.memoriadb.id.*;

/**
 * @author Sandro
 */
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
    return (int) (fValue ^ (fValue >>> 32));
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
