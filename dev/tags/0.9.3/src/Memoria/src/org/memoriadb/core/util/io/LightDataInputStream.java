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

/**
 * @author Micha
 */
public class LightDataInputStream extends FilterInputStream implements IDataInput {

  private static final ThreadLocal<byte[]> sLongBuffer = new ThreadLocal<byte[]>() {
    @Override
    protected byte[] initialValue() {
      return new byte[8];
    }
  };

  public LightDataInputStream(byte[] body) {
    this(new ByteArrayInputStream(body));
  }

  /**
   * Creates a DataInputStream that uses the specified underlying InputStream.
   * 
   * @param input
   *          the specified input stream
   */
  public LightDataInputStream(InputStream input) {
    super(input);
  }

  @Override
  public int read(byte b[]) throws IOException {
    return in.read(b, 0, b.length);
  }

  @Override
  public int read(byte b[], int off, int len) throws IOException {
    return in.read(b, off, len);
  }

  @Override
  public boolean readBoolean() throws IOException {
    int ch = in.read();
    if (ch < 0) throw new EOFException();
    return (ch != 0);
  }

  @Override
  public byte readByte() throws IOException {
    int ch = in.read();
    if (ch < 0) throw new EOFException();
    return (byte) (ch);
  }

  @Override
  public char readChar() throws IOException {
    int ch1 = in.read();
    int ch2 = in.read();
    if ((ch1 | ch2) < 0) throw new EOFException();
    return (char) ((ch1 << 8) + (ch2 << 0));
  }

  @Override
  public double readDouble() throws IOException {
    return Double.longBitsToDouble(readLong());
  }

  @Override
  public float readFloat() throws IOException {
    return Float.intBitsToFloat(readInt());
  }

  @Override
  public void readFully(byte b[]) throws IOException {
    readFully(b, 0, b.length);
  }

  @Override
  public void readFully(byte b[], int off, int len) throws IOException {
    if (len < 0) throw new IndexOutOfBoundsException();
    int n = 0;
    while (n < len) {
      int count = in.read(b, off + n, len - n);
      if (count < 0) throw new EOFException();
      n += count;
    }
  }

  @Override
  public int readInt() throws IOException {
    int ch1 = in.read();
    int ch2 = in.read();
    int ch3 = in.read();
    int ch4 = in.read();
    if ((ch1 | ch2 | ch3 | ch4) < 0) throw new EOFException();
    return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
  }

  @Override
  public String readLine() {
    throw new UnsupportedOperationException();
  }

  @Override
  public long readLong() throws IOException {
    byte[] longBuffer = getLongBuffer();
    readFully(longBuffer, 0, 8);
    return (((long) longBuffer[0] << 56) + ((long) (longBuffer[1] & 255) << 48) + ((long) (longBuffer[2] & 255) << 40)
        + ((long) (longBuffer[3] & 255) << 32) + ((long) (longBuffer[4] & 255) << 24) + ((longBuffer[5] & 255) << 16)
        + ((longBuffer[6] & 255) << 8) + ((longBuffer[7] & 255) << 0));
  }

  @Override
  public short readShort() throws IOException {
    int ch1 = in.read();
    int ch2 = in.read();
    if ((ch1 | ch2) < 0) throw new EOFException();
    return (short) ((ch1 << 8) + (ch2 << 0));
  }

  @Override
  public int readUnsignedByte() throws IOException {
    int ch = in.read();
    if (ch < 0) throw new EOFException();
    return ch;
  }

  @Override
  public int readUnsignedShort() throws IOException {
    int ch1 = in.read();
    int ch2 = in.read();
    if ((ch1 | ch2) < 0) throw new EOFException();
    return (ch1 << 8) + (ch2 << 0);
  }

  @Override
  public String readUTF() throws IOException {
    return DataInputStream.readUTF(this);
  }

  @Override
  public int skipBytes(int n) throws IOException {
    int total = 0;
    int cur = 0;

    while ((total < n) && ((cur = (int) in.skip(n - total)) > 0)) {
      total += cur;
    }

    return total;
  }

  @Override
  public IDataInput subInput(int byteCount) throws IOException {
    byte[] data = new byte[byteCount];
    readFully(data);
    return new ByteBufferDataInput(data);
  }

  private byte[] getLongBuffer() {
    return sLongBuffer.get();
  }
  
}
