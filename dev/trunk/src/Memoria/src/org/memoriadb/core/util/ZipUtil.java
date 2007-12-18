package org.memoriadb.core.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.*;

public class ZipUtil {
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
        }
    }
    
    // Get the decompressed data
    return bos.toByteArray();
  }
}
