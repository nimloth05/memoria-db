package org.memoriadb.core.util.io;

import java.io.*;

import org.memoriadb.core.exception.MemoriaException;

public class MemoriaByteArrayOutputStream extends ByteArrayOutputStream implements DataOutput {

  private static final int UNDEFINED_POSITION = -1;
  private int fPosition = UNDEFINED_POSITION;
  private final DataOutputStream fStream;

  public MemoriaByteArrayOutputStream() {
    fStream = new DataOutputStream(this);
  }

  public void setMarker() throws IOException {
    fPosition = count;
    fStream.writeInt(0);
  }

  public final void writeBoolean(boolean v) throws IOException {
    fStream.writeBoolean(v);
  }

  public final void writeByte(int v) throws IOException {
    fStream.writeByte(v);
  }

  public final void writeBytes(String s) throws IOException {
    fStream.writeBytes(s);
  }

  public final void writeChar(int v) throws IOException {
    fStream.writeChar(v);
  }

  public final void writeChars(String s) throws IOException {
    fStream.writeChars(s);
  }

  public final void writeDouble(double v) throws IOException {
    fStream.writeDouble(v);
  }

  public final void writeFloat(float v) throws IOException {
    fStream.writeFloat(v);
  }

  public final void writeInt(int v) throws IOException {
    fStream.writeInt(v);
  }

  public final void writeLong(long v) throws IOException {
    fStream.writeLong(v);
  }

  public void writeAtMarker(int data) throws IOException {
    if (fPosition == UNDEFINED_POSITION) throw new MemoriaException("You have to mark the position first");
    int tempPosition = count;
    count = fPosition;
    fStream.writeInt(data);
    count = tempPosition;
    fPosition = UNDEFINED_POSITION;
  }

  public final void writeShort(int v) throws IOException {
    fStream.writeShort(v);
  }

  public final void writeUTF(String str) throws IOException {
    fStream.writeUTF(str);
  }

}
