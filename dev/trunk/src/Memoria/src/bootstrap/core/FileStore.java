package bootstrap.core;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class FileStore implements IContext {
  
  private static final byte[] BLOCK_START_TAG = new byte[] {1, 2,3, 4};
  private static final byte[] BLOCK_END_TAG = new byte[] {4, 3, 2, 1};
  
  private static final byte[] TRANSACTION_START_TAG = new byte[] {5, 6, 7, 8};
  private static final byte[] TRANSACTION_END_TAG = new byte[] {8, 7, 6, 5};
  
  private MetaData fMetaData = new MetaData();
  private ObjectRepo fObjectRepo = new ObjectRepo();
  
  private final File fFile;
  
  private Set<HydratedObject> fHydratedObjects = new HashSet<HydratedObject>();
  private List<ObjectReference> fObjectsToBind = new ArrayList<ObjectReference>();
  
  public FileStore(File file) {
    fFile = file;
    fMetaData.bootstrap(this);
  }
  
  public void writeObject(Object... objects) {
    writeObject(Arrays.asList(objects));
  }
  
  public void writeObject(List<Object> objects) {
    try {
      internalWriteObject(objects);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("exception occured during write back: " + e);
    }
  }

  public void open() {
    try {
      readMetaInfo();
      dehydrateObjects();
      bindObjects();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private void bindObjects() throws Exception {
    for(ObjectReference ref: fObjectsToBind) {
      ref.bind(this);
    }
    fObjectsToBind.clear();
  }

  private void dehydrateObjects() throws Exception {
    for(HydratedObject object: fHydratedObjects) {
      object.dehydrate(this);
    }
    fHydratedObjects.clear();
  }

  private void readMetaInfo() throws Exception {
    int bufferSize = (int)Runtime.getRuntime().freeMemory() / 16;
    DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(fFile), bufferSize));

    byte[] blockTagBuffer = new byte[4];
    
    while (stream.available() > 0) {
      stream.read(blockTagBuffer);
      if (!Arrays.equals(blockTagBuffer, BLOCK_START_TAG)) throw new RuntimeException("Could not read block start");
      
      int blockSize = stream.readInt(); //the block size
      byte[] blockData = new byte[blockSize];
      stream.read(blockData); 
      readTransactionData(blockData);
      
      stream.read(blockTagBuffer);
      if (!Arrays.equals(blockTagBuffer, BLOCK_END_TAG)) throw new RuntimeException("Could not read block end Tag");
    }
    
    stream.close();
  }

  private void readTransactionData(byte[] data) throws Exception {
    byte[] transactionTagBuffer = new byte[4];
    
    System.arraycopy(data, 0, transactionTagBuffer, 0, 4);
    if (!Arrays.equals(transactionTagBuffer, TRANSACTION_START_TAG));
    
    //TODO We must read the transaction size here.
    
    System.arraycopy(data, data.length-4, transactionTagBuffer, 0, 4);
    if (!Arrays.equals(transactionTagBuffer, TRANSACTION_END_TAG)) throw new RuntimeException("could not read end transaction tag");
    
    readObjects(data, 8, data.length-12);
  }

  private void readObjects(byte[] data, int offset, int length) throws Exception {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, length));
    while(stream.available() > 0) {
      int size = stream.readInt();
      
      readObject(data, offset+4, size);
      offset += 4 + size;
      if (stream.skip(size) != size) throw new RuntimeException("could not skip bytes: " + size);
    }
  }

  private void readObject(byte[] data, int offset, int size) throws Exception {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    long typeId = stream.readLong();
    long objectId = stream.readLong();

    if(typeId == MetaClass.METACLASS_OBJECT_ID) {
      fMetaData.readMetaClass(this, stream, objectId);
      return;
    }
    fHydratedObjects.add(new HydratedObject(typeId, objectId, stream));
  }

  private void internalWriteObject(List<Object> objects) throws Exception {
    byte[] data = serializeObjects(objects);
    append(data);
  }

  private void append(byte[] data) throws IOException {
    RandomAccessFile file = new RandomAccessFile(fFile, "rw");
    
    long length = file.length();
    if (length > 0) {
      file.seek(file.length()-1);
    }
    
    file.write(BLOCK_START_TAG);
    int size = data.length+TRANSACTION_START_TAG.length+4+TRANSACTION_END_TAG.length; //the block is as big as the transaction data.
    file.writeInt(size); 
    file.write(TRANSACTION_START_TAG);
    file.writeInt(data.length);
    
    file.write(data);
    
    file.write(TRANSACTION_END_TAG);
    file.write(BLOCK_END_TAG);
    
    file.getFD().sync();
    file.close();
  }

  private byte[] serializeObjects(List<Object> objects) throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    DataOutput stream = new DataOutputStream(buffer);
    for(Object object: objects) {
      serializeObject(stream, object);
    }
    return buffer.toByteArray();
  }

  public void serializeObject(DataOutput dataStream, Object object) throws Exception {
    Class<?> type = object.getClass();
    long objectId = fObjectRepo.register(object);
    
    MetaClass metaClass = fMetaData.register(this, dataStream, type);
    
    metaClass.writeObject(this, dataStream, object, objectId);
  }

  @Override
  public long getObjectId(Object obj) {
    return fObjectRepo.getObjectId(obj);
  }

  @Override
  public void put(long objectId, Object obj) {
    fObjectRepo.put(objectId, obj);
  }

  public Collection<MetaClass> getMetaClass() {
    return fObjectRepo.getMetaObejcts();
  }

  @Override
  public MetaClass getMetaObject(Class<?> javaType) {
    return fObjectRepo.getMetaObject(javaType);
  }

  @Override
  public Object getObejctById(long objectId) {
    return fObjectRepo.getObjectById(objectId);
  }

  @Override
  public long register(Object object) {
    return fObjectRepo.register(object);
  }

  @Override
  public void objectToBind(Object object, Field field, long targetId) {
    fObjectsToBind.add(new ObjectReference(object, field, targetId));
  }
  
}
