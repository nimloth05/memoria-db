package org.memoriadb.test.core;

import java.util.List;

import org.memoriadb.IFilter;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.test.core.testclasses.composite.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class CompositeTest extends AbstractObjectStoreTest {
  
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
    //save(root, comp1, comp2, leaf1, leaf1.getTestObj());
    
    reopen();
    
    List<IComponent> allComponents = getAll(IComponent.class);
    assertEquals(4, allComponents.size());
    
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
