package org.memoriadb.test.core.block;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.OneInt;
import org.memoriadb.testutil.*;
import org.memoriadb.testutil.FileStructure.ObjectInfo;

public class TransactionWriterTest extends AbstractObjectStoreTest {

  public void test_object_info(){
    Object obj = new Object();
    IObjectId id = save(obj);
    delete(obj);
    
    FileStructure file = new FileStructure(getFile());
    assertEquals(2, file.getBlockCount());
    
    ObjectInfo o1 = file.getBlock(0).getObject(0);
    ObjectInfo o2 = file.getBlock(1).getObject(0);
    
    assertEquals(false, o1.isClass());
    assertEquals(false, o1.isDeleteMarker());
    assertEquals(1, o1.getVersion());
    assertEquals(id, o1.getId());

    assertEquals(false, o2.isClass());
    assertEquals(true, o2.isDeleteMarker());
    assertEquals(2, o2.getVersion());
    assertEquals(id, o2.getId());
  }
  
  public void test_opo_and_opf() {
    save(new Object());
    save(new OneInt(0)); // add class
    save(new OneInt(0));
    
    FileStructure file = new FileStructure(getFile());
    assertEquals(getOPO(), file.getBlock(0).getObject(0).getSize());
    
    // opf ist 4 byte
    assertEquals(getOPO() + getOPF() + 4, file.getBlock(2).getObject(0).getSize());
    
  } 
  
  public void test_position() {
    long headerSize = getFile().getSize();
    
    save(new Object());
    save(new Object());

    FileStructure file = new FileStructure(getFile());
    
    long positionB0 = file.getBlock(0).getPosition();
    long positionB1 = file.getBlock(1).getPosition();
    
    long blockSize = positionB1 - positionB0;
    
    assertEquals(headerSize, positionB0);
    assertEquals(positionB0+blockSize, positionB1);
    assertEquals(positionB1+blockSize, getFile().getSize());
  }
  
}