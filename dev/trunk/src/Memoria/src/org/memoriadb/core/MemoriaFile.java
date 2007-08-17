package org.memoriadb.core;

import java.util.*;

import org.memoriadb.core.backend.Block;

public class MemoriaFile {
  
  private final List<Block> fBlocks = new ArrayList<Block>();
  
  public void add(Block block) {
    if (block == null) throw new IllegalArgumentException("Block was null");
    fBlocks.add(block);
  }
  
  public Iterable<Block> getBlocks() {
    return Collections.unmodifiableList(fBlocks);
  }
  
}
