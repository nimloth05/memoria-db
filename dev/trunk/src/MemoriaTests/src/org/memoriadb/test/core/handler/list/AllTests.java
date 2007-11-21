package org.memoriadb.test.core.handler.list;

import junit.framework.*;

import org.memoriadb.test.core.handler.set.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.handler.list");
    //$JUnit-BEGIN$
    suite.addTestSuite(CopyOnWriteArrayListTest.class);
    suite.addTestSuite(VectorTest.class);
    suite.addTestSuite(StackTest.class);
    suite.addTestSuite(LinkedListTest.class);
    suite.addTestSuite(ArrayListTest.class);
    suite.addTestSuite(HashSetTest.class);
    suite.addTestSuite(TreeSetTest.class);
    //$JUnit-END$
    return suite;
  }

}
