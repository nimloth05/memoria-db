package org.memoriadb.core.block;

import java.io.*;

import org.memoriadb.block.Block;

public interface IBlockErrorHandler {
  
  public long blockSizeCorrupt(DataInputStream input, Block block) throws IOException;
  
  public long blockTagCorrupt(DataInputStream input, Block block) throws IOException;
  
  public void transactionCorrupt(DataInputStream input, Block block) throws IOException;
  
}
