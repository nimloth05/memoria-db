package org.memoriadb.core.file;

/**
 * 
 * @author msc
 *
 */
public interface ICompressor {
  
  public static class NullCompressor implements ICompressor {

    @Override
    public byte[] compress(byte[] input) {
      return input;
    }

    @Override
    public byte[] decompress(byte[] input) {
      return input;
    }
    
  }
  
  public static ICompressor NullComporeesorInstance = new NullCompressor();
  
  public byte[] compress(byte[] input);
  
  public byte[] decompress(byte[] input);
  
}
