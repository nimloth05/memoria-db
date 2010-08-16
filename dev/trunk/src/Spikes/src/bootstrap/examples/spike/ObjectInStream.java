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
import java.lang.reflect.Field;
import java.util.*;

public class ObjectInStream implements Closeable {
  
  private final DataInputStream fStream;

  public ObjectInStream(File file) throws FileNotFoundException {
    fStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
  }
  
  public List<Object> readAllObejcts() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException {
    List<Object> result = new ArrayList<Object>();

    while (fStream.available() > 0) {
      int size = fStream.readInt();
      byte[] data = new byte[size];
      fStream.read(data);
      
      ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
      DataInputStream in = new DataInputStream(byteStream);
      
      String className = in.readUTF();
      Class<?> clazz = Class.forName(className);
      
      result.add(createObject(in, clazz));
      
    }
    return result;
  }

  private Object createObject(DataInputStream in, Class<?> clazz) throws IOException, InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException {
    Object obj = clazz.newInstance();

    while (in.available() > 0) {
      String fieldName = in.readUTF();
      if (fieldName.isEmpty()) throw new RuntimeException("Empty Field found");
      
      Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      
      Class<?> fieldType = field.getType();
      
      if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
        field.set(obj, in.readInt());
        continue;
      }
      
      if (String.class.equals(fieldType)) {
        field.set(obj, in.readUTF());
      }
      
    }
    return obj;
  }

  @Override
  public void close() throws IOException {
    fStream.close();
  }
  
  

}
