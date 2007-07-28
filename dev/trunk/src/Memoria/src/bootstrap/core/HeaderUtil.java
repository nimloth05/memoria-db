package bootstrap.core;

public final class HeaderUtil {
  
  public static final byte[] BLOCK_START_TAG = new byte[] {1, 2,3, 4};
  public static final byte[] BLOCK_END_TAG = new byte[] {4, 3, 2, 1};
  public static final byte[] TRANSACTION_START_TAG = new byte[] {5, 6, 7, 8};
  public static final byte[] TRANSACTION_END_TAG = new byte[] {8, 7, 6, 5};

  private HeaderUtil() {}
  

}
