package org.memoriadb.test.core.block;

import java.util.TreeSet;

import junit.framework.*;

import org.memoriadb.block.Block;
import org.memoriadb.block.maintenancefree.BlockBucket;
import org.memoriadb.core.block.*;
import org.memoriadb.core.exception.MemoriaException;

public class BlockTest extends TestCase {
  
  public void test_compare() {
    TreeSet<BlockBucket> set = new TreeSet<BlockBucket>();
    
    BlockBucket block1 = new BlockBucket(1);
    set.add(block1);
    BlockBucket block10 = new BlockBucket(10);
    set.add(block10);
    BlockBucket block100 = new BlockBucket(100);
    set.add(block100);
    
    assertEquals(block1, set.ceiling(new BlockBucket(1)));
    assertEquals(block10, set.ceiling(new BlockBucket(2)));
    assertEquals(block10, set.ceiling(new BlockBucket(10)));
    assertEquals(block100, set.ceiling(new BlockBucket(20)));
    assertEquals(block100, set.ceiling(new BlockBucket(100)));
    assertNull(set.ceiling(new BlockBucket(101)));
  }
  
  public void test_too_many_inactive_ObjectData_throws() {
    Block block = new Block(0,0);
    block.setNumberOfObjectData(1);
    
    block.incrementInactiveObjectDataCount();
    
    try {
      block.incrementInactiveObjectDataCount();
      throw new AssertionFailedError("exception expected");
    }
    catch(MemoriaException e) {
      //pass
    }
    
  }
  
}
