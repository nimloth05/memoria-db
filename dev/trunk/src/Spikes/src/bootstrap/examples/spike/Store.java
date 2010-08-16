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

public final class Store {
  
  private File fFile;
  private Map<Long, String> fTypeInfo = new HashMap<Long, String>();

  public Store(File file) {
    fFile = file;
  }
  
  public void readData() throws FileNotFoundException {
    InputStream in = new BufferedInputStream(new FileInputStream(fFile), 100*1024);
    try {
      readData(in);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      saveClose(in);
    }
  }
  
  public void saveData(List<Class<?>> classes) throws FileNotFoundException {
    OutputStream out = new BufferedOutputStream(new FileOutputStream(fFile), 100*1024);
    try {
      writeData(out, classes);
    } catch(Exception e) {
      throw new RuntimeException(e);
    } finally {
      saveClose(out);
    }
  }

  private void writeData(OutputStream out, List<Class<?>> classes) throws IOException {
    writeTypeData(out, classes);
  }

  private void writeTypeData(OutputStream out, List<Class<?>> classes) throws IOException {
    long typeId = 0;
    DataOutputStream dataOut = new DataOutputStream(out);
    for(Class<?> clazz: classes) {
      dataOut.writeLong(++typeId);
      dataOut.writeUTF(clazz.getName());
    }
    dataOut.flush();
  }

  private void readData(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    
    int data;
    while ((data = in.read()) != -1 || data == 0) {
      out.write(data);
    }
    buildTypeInfo(new ByteArrayInputStream(out.toByteArray()));
  }

  private void buildTypeInfo(ByteArrayInputStream data) throws IOException {
    DataInputStream input = new DataInputStream(data);
    
    while (data.available() > 0) {
      long typeId = input.readLong();
      String className = input.readUTF();
      fTypeInfo.put(typeId, className);
    }
  }

  private void saveClose(Closeable in) {
    try {
      in.close();
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  public Map<Long, String> getTypeMap() {
    return Collections.unmodifiableMap(fTypeInfo);
  }

}
