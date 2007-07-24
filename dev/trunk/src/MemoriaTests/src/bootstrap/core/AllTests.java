package bootstrap.core;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for bootstrap.core");
    //$JUnit-BEGIN$
    suite.addTestSuite(LoadTest.class);
    suite.addTestSuite(ObjectRepoTest.class);
    suite.addTestSuite(UtilTest.class);
    suite.addTestSuite(FieldTypeTest.class);
    suite.addTestSuite(FileStoreTest.class);
    //$JUnit-END$
    return suite;
  }

}
