package org.memoriadb.test.core;

import java.util.List;

import org.memoriadb.IFilter;
import org.memoriadb.core.DBMode;
import org.memoriadb.core.handler.def.field.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.test.core.testclasses.composite.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class CompositeTest extends AbstractObjectStoreTest {
  
  public void test_composite_in_db_mode() {
    test_save_composite();
    
    reopen(DBMode.data);
    
    List<Object> allComposites = fStore.getAll(Composite.class.getName());
    assertEquals(3, allComposites.size());
    
    IFieldObject loadedRoot = (IFieldObject) fStore.getAll(Composite.class.getName(), new IFilter<Object>() {

      @Override
      public boolean accept(Object object) {
        IFieldObject obj = (IFieldObject)object;
        return obj.get("fData").equals("root");     
      }
    }).get(0);
    
    assertNotNull(loadedRoot);
    
    List<Object> list = (List<Object>) loadedRoot.get("fComponents");
    assertEquals(2, list.size());
    
    IFieldObject fieldObject1 = (IFieldObject) list.get(0);
    IFieldObject fieldObject2 = (IFieldObject) list.get(1);
    
    assertEquals("comp1", fieldObject1.get("fData"));
    assertEquals("comp2", fieldObject2.get("fData"));
    
    list = (List<Object>) fieldObject1.get("fComponents");
    assertEquals(1, list.size());
    
    IFieldObject leaf1 = (IFieldObject) list.get(0);
    assertEquals("leaf for comp1", leaf1.get("fData"));
    
    //UPDATE the objects
    fStore.beginUpdate();

    fieldObject1.set("fData", "changed value");
    save(fieldObject1);
    
    IObjectId memoriaClassIdForLeafObject = fStore.getObjectId(fStore.getMemoriaClass(leaf1)); 
    IFieldObject leaf2 = new FieldMapDataObject(memoriaClassIdForLeafObject);
    leaf2.set("fData", "leaf2");
    save(leaf2);
    
    list = (List<Object>) fieldObject2.get("fComponents"); 
    list.add(leaf2);
    save(list);
    
    fStore.endUpdate();
    
    reopen();
    
    List<Leaf> allLeafs = fStore.getAll(Leaf.class);
    assertEquals(2, allLeafs.size());
    
  }
  
  public void test_save_composite() {
    Composite root = new Composite();
    root.setData("root");
    
    Composite comp1 = new Composite();
    comp1.setData("comp1");
    root.addChild(comp1);
    
    Composite comp2 = new Composite();
    comp2.setData("comp2");
    root.addChild(comp2);
    
    Leaf leaf1 = new Leaf();
    leaf1.setData("leaf for comp1");
    leaf1.setTestObj(new SimpleTestObj("Leaf1 TestData"));
    
    comp1.addChild(leaf1);
    
    saveAll(root);
    
    reopen();
    
    List<Composite> allComposites = getAll(Composite.class);
    assertEquals(3, allComposites.size());
    
    Composite loadedRoot = getAll(Composite.class, new IFilter<Composite>() {

      @Override
      public boolean accept(Composite object) {
        return object.getData().equals("root");     
      }
      
    }).get(0);
    assertNotNull(loadedRoot);
    
    assertCompositeObject(root, loadedRoot);
    assertCompositeObject(comp1, loadedRoot.getChild(0));
    assertCompositeObject(comp2, loadedRoot.getChild(1));
    assertCompositeObject(leaf1, ((Composite) loadedRoot.getChild(0)).getChild(0));
  }

  private void assertCompositeObject(IComponent original, IComponent loaded) {
    assertEquals(original.getChildCount(), loaded.getChildCount());
    assertEquals(original.getData(), loaded.getData());
  }

}
