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
        continue;
      }
      
    }
    return obj;
  }

  public void close() throws IOException {
    fStream.close();
  }
  
  

}
