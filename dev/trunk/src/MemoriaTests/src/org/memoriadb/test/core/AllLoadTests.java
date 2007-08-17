package org.memoriadb.test.core;

import org.memoriadb.loadtests.LoadTest;

import junit.framework.*;

public class AllLoadTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.core");
    //$JUnit-BEGIN$
    suite.addTestSuite(LoadTest.class);
    //$JUnit-END$
    return suite;
  }

}
