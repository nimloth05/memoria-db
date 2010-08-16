
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

package bootstrap.examples.jos;

import java.io.*;

public class LogObjectInputStream extends ObjectInputStream {

  public LogObjectInputStream(InputStream in) throws IOException {
    super(in);
    enableResolveObject(true);
  }

  @Override
  protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    //System.out.println("resolveClass: " + desc);
    return super.resolveClass(desc);
  }

  @Override
  protected Object resolveObject(Object obj) throws IOException {
    Object result = super.resolveObject(obj);
    System.out.println("resolveObject: " + obj + " -> " + result);
    if(result instanceof B) return null;
    return result;
  }
  
  

  @Override
  protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
    System.out.println("resolveProxyClass");
    return super.resolveProxyClass(interfaces);
  }

  
  
}
