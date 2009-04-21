package org.memoriadb.test.handler.jdk.awt.color;

import java.awt.Color;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.ObjectReferencer;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class ColorHandlerTest extends AbstractMemoriaTest {
  
  public void test_save_color() {
    IObjectId id = saveColor(Color.blue);
    reopen();
    
    ObjectReferencer ref_l1 = get(id);
    Color color_l1 = (Color) ref_l1.getObject();
    assertEquals(Color.blue, color_l1);
  }
  
  private IObjectId saveColor(Color color) {
    ObjectReferencer ref = new ObjectReferencer();
    
    ref.setObject(color);
    IObjectId refId = fObjectStore.saveAll(ref);
    return refId;
  }

}
