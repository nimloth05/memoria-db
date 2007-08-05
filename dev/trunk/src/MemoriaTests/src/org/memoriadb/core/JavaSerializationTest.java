package org.memoriadb.core;

import java.util.List;

import org.memoriadb.core.testclasses.SimpleTestObj;
import org.memoriadb.core.testclasses.serialization.*;

/**
 * Mem or "Memoria" means a object that is handle by the default memoria serialization.
 * Ser or "Serialitation" menas a object that is handled by the java object serialization.
 * @author nienor
 *
 */
public class JavaSerializationTest extends AbstractObjectStoreTest {
  
  public void test_memoria_container_with_memoria_container() {
    MemoriaContainer container = new MemoriaContainer();
    SimpleTestObj obj = new SimpleTestObj("1");
    container.fElements.add(obj);
    
    save(container);
    
    long objId = fStore.getObjectId(obj);
    
    reopen();
    
    assertEquals(obj, fStore.getObjectById(objId));
  }
  
  public void test_serialize_java_container() {
    SerializationContainer container = new SerializationContainer();
    Serialization obj = new Serialization();
    obj.fString = "1";
    obj.fInt = 1;
    container.fElements[0] = obj;
    
    save(container);
    
    reopen();
    
    SerializationContainer loadedContainer = getAll(SerializationContainer.class).get(0);
    assertNotNull(loadedContainer.fElements[0]);
    assertEquals(obj.fString, loadedContainer.fElements[0].fString);
    assertEquals(obj.fInt, loadedContainer.fElements[0].fInt);
  }
  
  public void test_serialize_java_entity() {
    Serialization obj = new Serialization();
    obj.fInt = 1;
    obj.fString = "1";
    
    save(obj);
    
    reopen();
    Serialization loaded = getAll(Serialization.class).get(0);
    assertEquals(obj, loaded);
  }
  
  public void test_sharedObjectss() {
    SimpleTestObj memObj1 = new SimpleTestObj("1");
    SimpleTestObj memObj2 = new SimpleTestObj("2");
    SimpleTestObj memObj3 = new SimpleTestObj("3");
    
    Serialization serObj1 = new Serialization("4");
    Serialization serObj2 = new Serialization("5");
    Serialization serObj3 = new Serialization("6");
    
    save(memObj1, serObj1, memObj2, serObj2, memObj3, serObj3);
    
    long serObjId2 = fStore.getObjectId(serObj2);
    long memObjId2 = fStore.getObjectId(memObj2);
    
    reopen();
    
    List<SimpleTestObj> loadedMemObjs = getAll(SimpleTestObj.class); 
    List<Serialization> loadedSerObjs = getAll(Serialization.class);
    
    assertEquals(3, loadedMemObjs.size());
    assertEquals(3, loadedSerObjs.size());
    
    assertTrue(loadedSerObjs.contains(fStore.getObjectById(serObjId2)));
    assertTrue(loadedMemObjs.contains(fStore.getObjectById(memObjId2)));
  }
  
}
