package bootstrap.examples.jos2;

import java.io.*;
import java.lang.reflect.*;

public final class ObjectOutStream implements Flushable, Closeable {
  
  //public static final byte[] HEADER_TAG       = new byte[] { (byte) 161, (byte) 184, (byte) 152, (byte) 105 };
  
  private final DataOutputStream fFileStrem;

  public ObjectOutStream(File file) throws FileNotFoundException {
    fFileStrem = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
  }
  
  public void write(Object obj) throws IOException, IllegalArgumentException, IllegalAccessException {
    ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(entryBuffer);
    
    Class<?> objClass = obj.getClass();
    
    dataOut.writeUTF(objClass.getName());
    
    Field[] declaredFields = objClass.getDeclaredFields();
    for(Field field: declaredFields) {
      if (field.getModifiers() == Modifier.STATIC) continue;
      
      field.setAccessible(true);
      
      dataOut.writeUTF(field.getName());
      writeFieldValue(dataOut, field.get(obj));
      
      field.setAccessible(false);
    }
    dataOut.flush();
    dataOut.close();
    
    
    
    fFileStrem.writeInt(entryBuffer.toByteArray().length);
    fFileStrem.write(entryBuffer.toByteArray());
  }

  private void writeFieldValue(DataOutputStream dataOut, Object object) throws IOException {
    if (object instanceof Integer) {
      dataOut.writeInt((Integer)object);
    }
    if (object instanceof String) {
      dataOut.writeUTF(object.toString());
    }
  }

  public void flush() throws IOException {
    fFileStrem.flush();
  }

  public void close() throws IOException {
    fFileStrem.close();
  }
   

}
