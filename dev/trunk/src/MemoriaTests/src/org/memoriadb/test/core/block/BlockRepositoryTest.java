package org.memoriadb.test.core.block;

import java.util.Arrays;

import junit.framework.TestCase;

import org.memoriadb.block.Block;
import org.memoriadb.core.ObjectInfo;
import org.memoriadb.core.block.BlockRepository;
import org.memoriadb.id.loong.LongId;

public class BlockRepositoryTest extends TestCase {

  private BlockRepository fBlockRepository;
  private Block fBlock1;
  private Block fBlock2;
  private ObjectInfo fObjectInfoA;
  private ObjectInfo fObjectInfoB;
  private ObjectInfo fObjectInfoC;

  public void test_add() {
    fBlockRepository.add(fObjectInfoA, fBlock1);
    fBlockRepository.add(fObjectInfoC, fBlock2);
    fBlockRepository.add(fObjectInfoB, fBlock1);
    
    assertEquals(fBlock1, fBlockRepository.getBlock(fObjectInfoA));
    assertEquals(fBlock1, fBlockRepository.getBlock(fObjectInfoB));
    assertEquals(fBlock2, fBlockRepository.getBlock(fObjectInfoC));
    
    assertEquals(Arrays.asList(fObjectInfoA, fObjectInfoB), fBlockRepository.getObjectInfos(fBlock1));
    assertEquals(Arrays.asList(fObjectInfoC), fBlockRepository.getObjectInfos(fBlock2));
  }
  
  public void test_empty_repo() {
    assertNull(fBlockRepository.getBlock(fObjectInfoA));
    assertTrue(fBlockRepository.getObjectInfos(fBlock1).isEmpty());
  }
  
  public void test_remove() {
    fBlockRepository.add(fObjectInfoA, fBlock1);
    fBlockRepository.add(fObjectInfoC, fBlock2);
    fBlockRepository.add(fObjectInfoB, fBlock1);
    
    fBlockRepository.remove(fObjectInfoB);

    assertEquals(fBlock1, fBlockRepository.getBlock(fObjectInfoA));
    assertNull(fBlockRepository.getBlock(fObjectInfoB));
    assertEquals(fBlock2, fBlockRepository.getBlock(fObjectInfoC));
    
    assertEquals(Arrays.asList(fObjectInfoA), fBlockRepository.getObjectInfos(fBlock1));
    assertEquals(Arrays.asList(fObjectInfoC), fBlockRepository.getObjectInfos(fBlock2));

    fBlockRepository.remove(fObjectInfoA);
    
    assertNull(fBlockRepository.getBlock(fObjectInfoA));
    assertEquals(Arrays.asList(), fBlockRepository.getObjectInfos(fBlock1));
  }
  
  public void test_update() {
    fBlockRepository.add(fObjectInfoA, fBlock1);
    fBlockRepository.add(fObjectInfoC, fBlock2);
    fBlockRepository.add(fObjectInfoB, fBlock1);

    fBlockRepository.update(fObjectInfoA, fBlock2);
    
    assertEquals(fBlock2, fBlockRepository.getBlock(fObjectInfoA));
    assertEquals(fBlock1, fBlockRepository.getBlock(fObjectInfoB));
    assertEquals(fBlock2, fBlockRepository.getBlock(fObjectInfoC));
    
    assertEquals(Arrays.asList(fObjectInfoB), fBlockRepository.getObjectInfos(fBlock1));
    assertEquals(Arrays.asList(fObjectInfoC, fObjectInfoA), fBlockRepository.getObjectInfos(fBlock2));
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    fBlockRepository = new BlockRepository();
    fBlock1 = new Block(1);
    fBlock2 = new Block(2);
    fObjectInfoA = new ObjectInfo(new LongId(7), new LongId(1), new Object());
    fObjectInfoB = new ObjectInfo(new LongId(8), new LongId(1), new Object());
    fObjectInfoC = new ObjectInfo(new LongId(9), new LongId(1), new Object());
  }
  
}
