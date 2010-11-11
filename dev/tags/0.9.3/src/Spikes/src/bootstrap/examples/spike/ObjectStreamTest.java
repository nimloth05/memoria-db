/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package bootstrap.examples.spike;

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
