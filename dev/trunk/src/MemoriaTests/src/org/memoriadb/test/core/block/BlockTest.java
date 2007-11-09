package org.memoriadb.test.core.block;

import java.util.TreeSet;

import junit.framework.TestCase;

import org.memoriadb.core.block.Block;

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
  
}
