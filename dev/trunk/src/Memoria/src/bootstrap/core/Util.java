package bootstrap.core;


public final class Util {
  
  public static int convertToInt(byte[] buffer) {
    int int1 = buffer[0] & 0xff;
    int int2 = buffer[1] & 0xff;
    int int3 = buffer[2] & 0xff;
    int int4 = buffer[3] & 0xff;
    
    return ((int1 << 24) + (int2 << 16) + (int3 << 8) + (int4 << 0));
  }

  private Util() {}

}
