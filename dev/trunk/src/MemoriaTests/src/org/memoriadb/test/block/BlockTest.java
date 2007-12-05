package org.memoriadb.test.block;

import java.util.TreeSet;

import junit.framework.TestCase;

import org.memoriadb.block.maintenancefree.BlockBucket;

public class BlockTest extends TestCase {
  
  public void test_compare() {
    TreeSet<BlockBucket> set = new TreeSet<BlockBucket>();
    
    BlockBucket block1 = new BlockBucket(1);
    set.add(block1);
    BlockBucket block10 = new BlockBucket(10);
    set.add(block10);
    BlockBucket block100 = new BlockBucket(100);
    set.add(block100);
    
    assertSame(block1, set.ceiling(new BlockBucket(1)));
    assertSame(block10, set.ceiling(new BlockBucket(2)));
    assertSame(block10, set.ceiling(new BlockBucket(10)));
    assertSame(block100, set.ceiling(new BlockBucket(20)));
    assertSame(block100, set.ceiling(new BlockBucket(100)));
    assertNull(set.ceiling(new BlockBucket(101)));
  }
  
}
