package org.memoriadb.block.maintenancefree;

import java.util.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.exception.MemoriaException;

public class BlockBucket implements Comparable<BlockBucket> {
  
  private final long fSize;
  private List<Block> fBlocks;
  
  public BlockBucket(long size) {
    fSize = size;
  }
  
  /**
   * Adding same {@link Block} several times is ignored.
   */
  public void add(Block block) {
    // optimization to avoid instantiation of the List in case when no Blocks are added (when BlockBucket is search-prototype)
    if(fBlocks == null) fBlocks = new ArrayList<Block>();
    
    if(fBlocks.contains(block)) return;
    
    fBlocks.add(block);
  }
  
  @Override
  public int compareTo(BlockBucket o) {
    return (fSize < o.fSize ? -1 : (fSize == o.fSize ? 0 : 1));
  }
  
  @Override
  public boolean equals(Object obj) {
    throw new UnsupportedOperationException();
  }

  public int getBlockCount() {
    return fBlocks.size();
  }

  public Iterable<Block> getBlocks() {
    return fBlocks;
  }

  public long getSize() {
    return fSize;
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException();
  }

  public boolean isEmpty() {
    return fBlocks.isEmpty();
  }

  public Block pop() {
    return fBlocks.remove(0);
  }

  public void remove(Block block) {
    if(!fBlocks.remove(block)) throw new MemoriaException("block not found: " + block);
  }
  
  
  
}