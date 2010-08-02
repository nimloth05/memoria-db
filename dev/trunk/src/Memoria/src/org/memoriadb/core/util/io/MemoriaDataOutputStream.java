/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.core.util.io;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.ByteUtil;
import org.memoriadb.core.util.MemoriaCRC32;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public class MemoriaDataOutputStream extends ByteArrayOutputStream implements DataOutput {

  private static final int UNDEFINED_POSITION = -1;
  private int fPosition = UNDEFINED_POSITION;
  private final DataOutputStream fStream;

  public MemoriaDataOutputStream() {
    fStream = new DataOutputStream(this);
  }

  public void setMarker() throws IOException {
    fPosition = count;
    fStream.writeInt(0);
  }

  public void updateCRC32FromMarkerToPosition(MemoriaCRC32 crc32) {
    checkPosition();
    int offset = fPosition + 4;
    crc32.update(buf, offset, count - offset);
  }

  public void writeAtMarker(int data) throws IOException {
    checkPosition();
    int tempPosition = count;
    count = fPosition;
    fStream.writeInt(data);
    count = tempPosition;
    fPosition = UNDEFINED_POSITION;
  }

  @Override
  public final void writeBoolean(boolean v) throws IOException {
    fStream.writeBoolean(v);
  }

  @Override
  public final void writeByte(int v) throws IOException {
    fStream.writeByte(v);
  }

  @Override
  public final void writeBytes(String s) throws IOException {
    fStream.writeBytes(s);
  }

  @Override
  public final void writeChar(int v) throws IOException {
    fStream.writeChar(v);
  }

  @Override
  public final void writeChars(String s) throws IOException {
    fStream.writeChars(s);
  }

  @Override
  public final void writeDouble(double v) throws IOException {
    fStream.writeDouble(v);
  }

  @Override
  public final void writeFloat(float v) throws IOException {
    fStream.writeFloat(v);
  }

  @Override
  public final void writeInt(int v) throws IOException {
    fStream.writeInt(v);
  }

  @Override
  public final void writeLong(long v) throws IOException {
    fStream.writeLong(v);
  }

  @Override
  public final void writeShort(int v) throws IOException {
    fStream.writeShort(v);
  }

  public void writeUnsignedLong(long l) throws IOException {
    ByteUtil.writeUnsignedLong(l, this);
  }
  
  @Override
  public final void writeUTF(String str) throws IOException {
    fStream.writeUTF(str);
  }

  private void checkPosition() {
    if (fPosition == UNDEFINED_POSITION) throw new MemoriaException("You have to mark the position first");
  }

}
