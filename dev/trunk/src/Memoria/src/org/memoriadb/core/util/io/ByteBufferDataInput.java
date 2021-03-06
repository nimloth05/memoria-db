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
package org.memoriadb.core.util.io;

import java.io.*;
import java.nio.*;

/**
 * A {@link ByteBufferDataInput} can be used instead of a {@link LightDataInputStream}
 * whenever the data is already in the memory, either as byte array or in a {@link ByteBuffer}.
 *
 * @author Micha 
 */
public class ByteBufferDataInput implements IDataInput {

//  public static final Charset CHARSET_UFT8 = Charset.forName("UTF-8");

  private final ByteBuffer fBuffer;

  public ByteBufferDataInput(byte[] data) {
    this(ByteBuffer.wrap(data));
  }

  public ByteBufferDataInput(ByteBuffer buffer) {
    fBuffer = buffer;
  }

  @Override
  public int available() {
    return fBuffer.remaining();
  }

  @Override
  public boolean readBoolean() throws IOException {
    try {
      return fBuffer.get() != 0;
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }
  }

  @Override
  public byte readByte() throws IOException {
    try {
      return fBuffer.get();
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }
  }

  @Override
  public char readChar() throws IOException {
    try {
      return fBuffer.getChar();
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }
  }

  @Override
  public double readDouble() throws IOException {
    try {
      return fBuffer.getDouble();
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }
  }

  @Override
  public float readFloat() throws IOException {
    try {
      return fBuffer.getFloat();
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }

  }

  @Override
  public void readFully(byte[] b) throws IOException {
    try {
      fBuffer.get(b);
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void readFully(byte[] b, int off, int len) throws IOException {
    try {
      fBuffer.get(b, off, len);
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }
  }

  @Override
  public int readInt() {
    return fBuffer.getInt();
  }

  @Override
  public String readLine() {
    throw new UnsupportedOperationException();
  }

  @Override
  public long readLong() throws IOException {
    try {
      return fBuffer.getLong();
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }
  }

  @Override
  public short readShort() throws IOException {
    try {
      return fBuffer.getShort();
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }
  }

  @Override
  public int readUnsignedByte() throws IOException {
    try {
      return fBuffer.get() & 0xFF;
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }
  }

  @Override
  public int readUnsignedShort() throws IOException {
    try {
      return fBuffer.getShort() & 0xFFFF;
    }
    catch (BufferUnderflowException e) {
      throw new IOException(e);
    }
  }

  @Override
  public String readUTF() throws IOException {
    return DataInputStream.readUTF(this);
//    int bytes = readUnsignedShort();
//    byte[] data = new byte[bytes];
//    fBuffer.get(data);
//    return new String(data, CHARSET_UFT8);
  }

  @Override
  public int skipBytes(int n) {
    int skipBytes = Math.min(n, fBuffer.remaining());
    fBuffer.position(fBuffer.position() + skipBytes);
    return skipBytes;
  }

  @Override
  public IDataInput subInput(int byteCount) throws IOException {
    try {
      ByteBuffer subBuffer = fBuffer.slice();
      subBuffer.limit(byteCount);
      skipBytes(byteCount);
      return new ByteBufferDataInput(subBuffer);
    }
    catch (IllegalArgumentException e) {
      throw new IOException(e);
    }
  }

}
