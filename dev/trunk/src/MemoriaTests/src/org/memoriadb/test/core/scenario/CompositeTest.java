package org.memoriadb.test.core.scenario;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.collection.IListDataObject;
import org.memoriadb.handler.field.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.SimpleTestObj;
import org.memoriadb.test.testclasses.composite.*;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class CompositeTest extends AbstractMemoriaTest {
  
  public void test_composite_in_db_mode() {
    test_save_composite();
    
    reopenDataMode();
    
    List<IDataObject> l1_allComposites = fDataStore.query(Composite.class.getName());
    assertEquals(3, l1_allComposites.size());
    
    List<IDataObject> l1_allLeafs = fDataStore.query(Leaf.class.getName());
    assertEquals(1, l1_allLeafs.size());
    
    IFieldbasedObject l1_root = fDataStore.query(Composite.class.getName(), new IFilter<IFieldbasedObject>() {

      @Override
      public boolean accept(IFieldbasedObject object, IFilterControl control) {
        IFieldbasedObject obj = object;
        return obj.get("fData").equals("root");     
      }
    }).get(0);
    
    assertNotNull(l1_root);
    
    IListDataObject listDataObject = (IListDataObject) l1_root.get("fComponents");
    List<Object> list = listDataObject.getList();
    assertEquals(2, list.size());
    
    IFieldbasedObject fieldObject1 = (IFieldbasedObject) list.get(0);
    IFieldbasedObject fieldObject2 = (IFieldbasedObject) list.get(1);
    
    assertEquals("comp1", fieldObject1.get("fData"));
    assertEquals("comp2", fieldObject2.get("fData"));
    
    listDataObject = (IListDataObject) fieldObject1.get("fComponents");
    list = listDataObject.getList();
    assertEquals(1, list.size());
    
    IFieldbasedObject l1_leaf1 = (IFieldbasedObject) list.get(0);
    assertEquals("leaf for comp1", l1_leaf1.get("fData"));
    
    //UPDATE the objects
    fDataStore.beginUpdate();

    l1_leaf1.set("fData", "dataModeLeaf");
    save(l1_leaf1);
    
    IObjectId memoriaClassIdForLeafObject = l1_leaf1.getMemoriaClassId();
    
    IFieldbasedObject leaf2 = new FieldbasedDataObject(memoriaClassIdForLeafObject);
    leaf2.set("fData", "dataModeLeaf");
    leaf2.set("fTestObj", l1_leaf1.get("fTestObj"));
    save(leaf2);
    
    listDataObject = (IListDataObject) fieldObject2.get("fComponents");
    list = listDataObject.getList();
    list.add(leaf2);
    save(listDataObject);
    
    fDataStore.endUpdate();
    
    reopen();
    
    List<Leaf> l2_allLeafs = fObjectStore.query(Leaf.class);
    assertEquals(2, l2_allLeafs.size());
    
    for(int i =0; i < l2_allLeafs.size(); ++i) {
      Leaf leaf = l2_allLeafs.get(i);
      assertEquals("dataModeLeaf", leaf.getData());
    }
    
    List<Composite> l2_allComposites = fObjectStore.query(Composite.class);
    assertEquals(3, l2_allComposites.size());
    
    assertSame(l2_allLeafs.get(0).getTestObj(), l2_allLeafs.get(1).getTestObj());
    
    Composite l2_root = fObjectStore.query(Composite.class, new IFilter<Composite>() {

      @Override
      public boolean accept(Composite object, IFilterControl control) {
        return object.getData().equals("root");     
      }
      
    }).get(0);
    
    assertEquals(2, l2_root.getChildCount());
    for(int i = 0; i < l2_root.getChildCount(); ++i) {
      Composite child = (Composite) l2_root.getChild(i);
      assertEquals(1, child.getChildCount());
    }
  }
  
  public void test_deleting_shared_object() {
    Composite c1 = new Composite();
    Composite c2 = new Composite();
    Leaf shared = new Leaf();
    c1.addChild(shared);
    c2.addChild(shared);
    
    saveAll(c1);
    saveAll(c2);
    
    deleteAll(c1);
    deleteAll(c2);
    
    assertEquals(0, fObjectStore.query(Object.class).size());
    
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
    
    List<Composite> allComposites = fObjectStore.query(Composite.class);
    assertEquals(3, allComposites.size());
    
    Composite loadedRoot = fObjectStore.query(Composite.class, new IFilter<Composite>() {

      @Override
      public boolean accept(Composite object, IFilterControl control) {
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
