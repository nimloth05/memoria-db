package org.memoriadb.test.core.block;

import java.util.TreeSet;

import junit.framework.*;

import org.memoriadb.core.block.Block;
import org.memoriadb.exception.MemoriaException;

public class BlockTest extends TestCase {
  
  public void test_compare() {
    TreeSet<Block> set = new TreeSet<Block>();
    
    Block block1 = new Block(1,0);
    set.add(block1);
    Block block10 = new Block(10,0);
    set.add(block10);
    Block block100 = new Block(100,0);
    set.add(block100);
    
    assertEquals(block1, set.ceiling(new Block(1,-1)));
    assertEquals(block10, set.ceiling(new Block(2,-1)));
    assertEquals(block10, set.ceiling(new Block(10,-1)));
    assertEquals(block100, set.ceiling(new Block(20,-1)));
    assertEquals(block100, set.ceiling(new Block(100,-1)));
    assertNull(set.ceiling(new Block(101,-1)));
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
