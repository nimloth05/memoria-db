package org.memoriadb.test.testclasses;

import java.util.*;

public class CollectionOwner {
  
  private Map<String, String> fMap = new HashMap<String, String>();
  private List<String> fList = new ArrayList<String>();
  
  public CollectionOwner() {
    
  }
  
  public CollectionOwner(Map<String, String> map, List<String> list) {
    fMap = map;
    fList = list;
  }
  
  public List<String> getList() {
    return fList;
  }
  public Map<String, String> getMap() {
    return fMap;
  }
  
}
