package org.memoriadb.test.handler.map;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.handler.map");
    //$JUnit-BEGIN$
    suite.addTestSuite(ConcurrentHashMapTest.class);
    suite.addTestSuite(HashMapTest.class);
    suite.addTestSuite(QueryTest.class);
    suite.addTestSuite(WeakHashMapTest.class);
    suite.addTestSuite(ConcurrentSkipListMapTest.class);
    suite.addTestSuite(IdentityHashMapTest.class);
    suite.addTestSuite(TreeMapTest.class);
    suite.addTestSuite(LinkedHashMapTest.class);
    suite.addTestSuite(LinkedHashMapOrderTest.class);
    //$JUnit-END$
    return suite;
  }

}
