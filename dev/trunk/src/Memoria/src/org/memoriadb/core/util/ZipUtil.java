/*
 * Copyright 2010 Sandro Orlando, Micha Riser
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

package org.memoriadb.core.util;

import java.io.*;
import java.util.zip.*;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.io.IOUtil;

/**
 * NOTE: Do not use {@link Deflater} and {@link Inflater} directly because of a performance bug as
 * described in bug 6751338 of the sun bug database. 
 */
public final class ZipUtil {
  
  public static byte[] compress(byte[] input) {
    Deflater deflater = new Deflater();
    try {
      return compress(input, deflater);
    } finally {
      deflater.end();
    }
  }
  
  public static byte[] compress(byte[] input, Deflater deflater) {
    ByteArrayInputStream in = new ByteArrayInputStream(input);
    ByteArrayOutputStream out = new ByteArrayOutputStream(input.length);
    DeflaterOutputStream deflatingStream = new DeflaterOutputStream(out, deflater, 4*1024);
    try {
      IOUtil.copyInputStreamToOutputStream(in, deflatingStream);
      return out.toByteArray();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }
  
  public static byte[] decompress(byte[] input) {
    Inflater inflater = new Inflater();
    try {
      return decompress(input, inflater);
    } finally {
      inflater.end();
    }
  }
  
  public static byte[] decompress(byte[] input, Inflater inflater) {
    ByteArrayInputStream in = new ByteArrayInputStream(input);
    ByteArrayOutputStream out = new ByteArrayOutputStream(input.length);
    InflaterInputStream inflatingStream = new InflaterInputStream(in, inflater, 4*1024);
    try {
      IOUtil.copyInputStreamToOutputStream(inflatingStream, out);
      return out.toByteArray();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }
  
  private ZipUtil() {}
}
