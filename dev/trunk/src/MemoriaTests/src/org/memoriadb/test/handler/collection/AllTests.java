package org.memoriadb.test.handler.collection;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.handler.list");
    //$JUnit-BEGIN$
    suite.addTestSuite(LinkedListTest.class);
    suite.addTestSuite(CopyOnWriteArraySetTest.class);
    suite.addTestSuite(LinkedHashSetTest.class);
    suite.addTestSuite(StackTest.class);
    suite.addTestSuite(VectorTest.class);
    suite.addTestSuite(TreeSetTest.class);
    suite.addTestSuite(CopyOnWriteArrayListTest.class);
    suite.addTestSuite(ArrayListTest.class);
    suite.addTestSuite(HashSetTest.class);
    suite.addTestSuite(ConcurrentSkipListSetTest.class);
    //$JUnit-END$
    return suite;
  }

}
