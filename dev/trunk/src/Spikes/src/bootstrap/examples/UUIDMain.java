package bootstrap.examples;

import java.util.*;

/**
 * $Rev$
 * @author msc
 *
 */
public class UUIDMain {
  
  public static void main(String[] args) {
    final int count = 10000;
    Set<String> uuids = new HashSet<String>();
    for(int i = 0; i < count; ++i) {
      UUID randomUUID = UUID.randomUUID();
      String uuid = randomUUID.toString();
      System.out.println(uuid);
      uuids.add(uuid);
    }
    System.out.println(uuids.size() == count);
  }

}
