package org.memoriadb.test.core.testclasses;

import java.lang.reflect.*;


public class Referencer {
  
  private Object fObject;
  
  public Object get() {
    return fObject;
  }
  
  public String getStringValueFromReferencee() throws Exception {
    Method declaredMethod = fObject.getClass().getDeclaredMethod("getString");
    return declaredMethod.invoke(fObject).toString();
  }
  
  public <T> void set(Class<T> referenceeType, String value) throws Exception {
    Constructor<T> constructor = referenceeType.getDeclaredConstructor(String.class);
    fObject = constructor.newInstance(value);
  }
  
  
}
 