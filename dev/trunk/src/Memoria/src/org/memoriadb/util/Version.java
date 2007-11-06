package org.memoriadb.util;

public class Version {
  
  private final int fMajor;
  private final int fMinor;
  private final int fService;
  
  public Version(int major, int minor, int service) {
    super();
    this.fMajor = major;
    this.fMinor = minor;
    this.fService = service;
  }

  public int getMajor() {
    return fMajor;
  }

  public int getMinor() {
    return fMinor;
  }

  public int getService() {
    return fService;
  }
  
}
