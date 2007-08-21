package org.memoriadb.core.load;

import java.io.*;
import java.util.*;

import org.memoriadb.core.*;
import org.memoriadb.core.backend.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.ByteUtil;


public final class FileReader implements IReaderContext {
  
  
  private Map<Long, HydratedInfo> fHydratedObjects = new HashMap<Long, HydratedInfo>();
  private Map<Long, HydratedInfo> fHydratedMetaClasses = new HashMap<Long, HydratedInfo>();
  
  private final Set<IBindable> fObjectsToBind = new HashSet<IBindable>();
  
  private ObjectRepo fRepo;
  private final MemoriaFile fMemoriaFile;
  private final IMemoriaFile fFile;


  public static void readIn(IMemoriaFile file, ObjectRepo repo) {
    new FileReader(file).read(repo);
  }
  
  private FileReader(IMemoriaFile file) {
    if (file == null) throw new IllegalArgumentException("File for readIn was null");
    fFile = file;
    fMemoriaFile = new MemoriaFile();
  }
  
  @Override
  public Object getObjectById(long objectId) {
    return fRepo.getObject(objectId);
  }
  
  @Override
  public void objectToBind(IBindable bindable) {
    fObjectsToBind.add(bindable);
  }

  public void read(ObjectRepo repo) {
    fRepo = repo;
    try {
      readBlockData();
      dehydrateMetaClasses();
      dehydrateObjects();
      bindObjects();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new MemoriaException(e);
    }
  }

  private void bindObjects() {
    try {
      for(IBindable ref: fObjectsToBind) {
        ref.bind(this);
      }
      fObjectsToBind.clear();
    } catch (Exception e) {
      throw new BindingException(e);
    }
  }

  private void dehydrateMetaClasses() throws Exception {
    for(HydratedInfo info: fHydratedMetaClasses.values()){
      dehydrateObject(info);
    }
    fHydratedMetaClasses = null;
  }
  
  private void dehydrateObject(HydratedInfo info) throws Exception {
    fRepo.add(info.getObjectId(), info.getHydratedObject().dehydrate(this), info.getVersion());
  }

  private void dehydrateObjects() throws Exception {
    for(HydratedInfo info: fHydratedObjects.values()) {
      dehydrateObject(info);
    }
    fHydratedObjects = null;
  }
  
  private void readBlockData() throws Exception {
    DataInputStream stream = new DataInputStream(fFile.getInputStream());

    int position = 0;
    
    while (stream.available() > 0) {
      
      HeaderUtil.assertTag(stream, HeaderUtil.BLOCK_START_TAG);
      
      int blockSize = stream.readInt(); //the block size
      position += 4;
      
      byte[] blockData = new byte[blockSize];
      stream.read(blockData); 
      position += blockSize;
      
      readTransactionData(blockData);

      HeaderUtil.assertTag(stream, HeaderUtil.BLOCK_END_TAG);
      
      fMemoriaFile.add(new Block(blockSize, position));
    }
    
    stream.close();
  }

  private void readObject(byte[] data, int offset, int size) throws Exception {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    long typeId = stream.readLong();
    long objectId = stream.readLong();

    if(MetaClass.isMetaClassObject(typeId)) {
      // FIXMEfix version
      fHydratedMetaClasses.put(objectId, new HydratedInfo(objectId, new HydratedObject(typeId, stream), 0));
      return;
    }
    
    // FIXME fix version
    fHydratedObjects.put(objectId, new HydratedInfo(objectId, new HydratedObject(typeId, stream), 0));
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
  
  private void readTransactionData(byte[] data) throws Exception {
    HeaderUtil.assertTag(data, 0, HeaderUtil.TRANSACTION_START_TAG);
    int transactionSize = ByteUtil.readInt(data, 4);
    final int endTagStart = transactionSize+ByteUtil.INT_SIZE+HeaderUtil.HEADER_SIZE;
    HeaderUtil.assertTag(data, endTagStart, HeaderUtil.TRANSACTION_END_TAG);
    
    readObjects(data, 8, transactionSize);
  }
}
