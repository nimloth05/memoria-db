package org.memoriadb.core.example;

import java.io.*;
import java.util.*;

import junit.framework.TestCase;

public class ObjectStreamTest extends TestCase {
  
  
  
  private static class NonSerializable {
    protected String fName;
    public NonSerializable() {}
    public NonSerializable(String name) {
      fName = name;
    }
  }
  
  private static class Person implements Serializable {
    protected String fName;
    protected int fPhoneNumber;
    protected Person fPartner;
    protected NonSerializable fNonSerializable;
    protected final List<Person> fFriends = new ArrayList<Person>();
    
    public Person() {}
    public Person(String name) {
      fName = name;
    }
  }
  
  
  public void test_replaceObjects() throws SecurityException, IOException {
    ObjectOutputStream stream = new ObjectOutputStream(new ByteArrayOutputStream()) {
      
      {
        super.enableReplaceObject(true);
      }

      @Override
      protected Object replaceObject(Object obj) throws IOException {
        System.out.println(obj.getClass());
        if (!(obj instanceof Serializable)) return new HashMap();
        return super.replaceObject(obj);
      }
    };
    
    Person person = new Person("Nicolai");
    person.fPartner = new Person("Isabel");
    person.fFriends.add(new Person("Godric"));
    person.fNonSerializable = new NonSerializable("Markal");
    
    stream.writeObject(person);
  }
  
  
  
  
  
  
  
  

}
