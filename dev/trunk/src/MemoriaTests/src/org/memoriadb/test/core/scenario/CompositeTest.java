package org.memoriadb.test.core.scenario;

import java.util.List;

import org.memoriadb.IFilter;
import org.memoriadb.core.DBMode;
import org.memoriadb.core.handler.def.IListDataObject;
import org.memoriadb.core.handler.def.field.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.test.core.testclasses.composite.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class CompositeTest extends AbstractObjectStoreTest {
  
  public void test_composite_in_db_mode() {
    test_save_composite();
    
    reopen(DBMode.data);
    
    List<Object> l1_allComposites = fStore.getAll(Composite.class.getName());
    assertEquals(3, l1_allComposites.size());
    
    List<Object> l1_allLeafs = fStore.getAll(Leaf.class.getName());
    assertEquals(1, l1_allLeafs.size());
    
    IFieldObject l1_root = (IFieldObject) fStore.getAll(Composite.class.getName(), new IFilter<Object>() {

      @Override
      public boolean accept(Object object) {
        IFieldObject obj = (IFieldObject)object;
        return obj.get("fData").equals("root");     
      }
    }).get(0);
    
    assertNotNull(l1_root);
    
    IListDataObject listDataObject = (IListDataObject) l1_root.get("fComponents");
    List<Object> list = listDataObject.getList();
    assertEquals(2, list.size());
    
    IFieldObject fieldObject1 = (IFieldObject) list.get(0);
    IFieldObject fieldObject2 = (IFieldObject) list.get(1);
    
    assertEquals("comp1", fieldObject1.get("fData"));
    assertEquals("comp2", fieldObject2.get("fData"));
    
    listDataObject = (IListDataObject) fieldObject1.get("fComponents");
    list = listDataObject.getList();
    assertEquals(1, list.size());
    
    IFieldObject l1_leaf1 = (IFieldObject) list.get(0);
    assertEquals("leaf for comp1", l1_leaf1.get("fData"));
    
    //UPDATE the objects
    fStore.beginUpdate();

    l1_leaf1.set("fData", "dataModeLeaf");
    save(l1_leaf1);
    
    IObjectId memoriaClassIdForLeafObject = l1_leaf1.getMemoriaClassId();
    
    IFieldObject leaf2 = new FieldMapDataObject(memoriaClassIdForLeafObject);
    leaf2.set("fData", "dataModeLeaf");
    leaf2.set("fTestObj", l1_leaf1.get("fTestObj"));
    save(leaf2);
    
    listDataObject = (IListDataObject) fieldObject2.get("fComponents");
    list = listDataObject.getList();
    list.add(leaf2);
    save(listDataObject);
    
    fStore.endUpdate();
    
    reopen(DBMode.clazz);
    
    List<Leaf> l2_allLeafs = fStore.getAll(Leaf.class);
    assertEquals(2, l2_allLeafs.size());
    
    for(int i =0; i < l2_allLeafs.size(); ++i) {
      Leaf leaf = l2_allLeafs.get(i);
      assertEquals("dataModeLeaf", leaf.getData());
    }
    
    List<Composite> l2_allComposites = fStore.getAll(Composite.class);
    assertEquals(3, l2_allComposites.size());
    
    assertSame(l2_allLeafs.get(0).getTestObj(), l2_allLeafs.get(1).getTestObj());
    
    Composite l2_root = getAll(Composite.class, new IFilter<Composite>() {

      @Override
      public boolean accept(Composite object) {
        return object.getData().equals("root");     
      }
      
    }).get(0);
    
    assertEquals(2, l2_root.getChildCount());
    for(int i = 0; i < l2_root.getChildCount(); ++i) {
      Composite child = (Composite) l2_root.getChild(i);
      assertEquals(1, child.getChildCount());
    }
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
    
    reopen(DBMode.clazz);
    
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
