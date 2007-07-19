package bootstrap.core;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for bootstrap.core");
    //$JUnit-BEGIN$
    suite.addTestSuite(ObjectRepoTest.class);
    suite.addTestSuite(FileStoreTest.class);
    suite.addTestSuite(FieldTypeTest.class);
    //$JUnit-END$
    return suite;
  }

}
