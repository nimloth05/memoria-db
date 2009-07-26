package org.memoriadb.test.crud.delete;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.ObjectReferencer;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

/**
 * The reason for this Test is to verify that the error-Message is detailed enough in case an referenced object was
 * deleted.
 *
 */
public class DeleteReferencedObjectTest extends AbstractMemoriaTest {

  public void test_error_message() {
    StringObject referencee = new StringObject("StringObject");
    ObjectReferencer referencer = new ObjectReferencer(referencee);

    IObjectId id = saveAll(referencer);
    delete(referencee);
    
    System.out.println(fObjectStore.getId(referencer));
    System.out.println(referencer.getObject());

    try {
      reopen();
      //fail("reopen() should throw because the referencee was deleted.");
    }
    catch(MemoriaException e) {
      System.out.println(e.getMessage());
    }
    
    referencer = fObjectStore.get(id);
    System.out.println(referencer.getObject());

  }

}
