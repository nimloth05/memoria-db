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

package org.memoriadb.core.util;

import org.memoriadb.core.exception.MemoriaException;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class ZipUtil {
  
  public static byte[] compress(byte[] input) {
    
    // Create the compressor with highest level of compression
    Deflater compressor = new Deflater();
    
    // Give the compressor the data to compress
    compressor.setInput(input);
    compressor.finish();
    
    // Create an expandable byte array to hold the compressed data.
    // You cannot use an array that's the same size as the orginal because
    // there is no guarantee that the compressed data will be smaller than
    // the uncompressed data.
    ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
    
    // Compress the data
    byte[] buf = new byte[1024];
    while (!compressor.finished()) {
        int count = compressor.deflate(buf);
        bos.write(buf, 0, count);
    }
    
    // Get the compressed data
    return bos.toByteArray();
  }
  
  public static byte[] decompress(byte[] input) {
    // Create the decompressor and give it the data to compress
    Inflater decompressor = new Inflater();
    decompressor.setInput(input);
    
    // Create an expandable byte array to hold the decompressed data
    ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
    
    // Decompress the data
    byte[] buf = new byte[1024];
    while (!decompressor.finished()) {
        try {
            int count = decompressor.inflate(buf);
            bos.write(buf, 0, count);
        } catch (DataFormatException e) {
          throw new MemoriaException(e);
        }
    }
    
    // Get the decompressed data
    return bos.toByteArray();
  }
  
  private ZipUtil() {}
}
