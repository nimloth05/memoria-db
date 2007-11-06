package org.memoriadb.core.file;

import java.util.UUID;

import org.memoriadb.core.id.IObjectIdFactory;
import org.memoriadb.util.*;


/**
 * Holds the information stored in the memoria file-header 
 * @author msc
 */
public class FileHeader {

  private final UUID fThisUuid;
  private final UUID fHostUuid;
  private final Version fVersion;
  private final int fFileLayoutVersion;
  private final String fIdFactoryClassName;
  private final String fBlockManagerClassName;
  private final int fHeaderSize;

  public FileHeader(UUID thisUuid, UUID hostUuid, Version version, int fileLayoutVersion, String idFactoryClassName, String blockManagerClassName, int headerSize) {
    fThisUuid = thisUuid;
    fHostUuid = hostUuid;
    fVersion = version;
    fFileLayoutVersion = fileLayoutVersion;
    fIdFactoryClassName = idFactoryClassName;
    fBlockManagerClassName = blockManagerClassName;
    fHeaderSize = headerSize;
  }

  public String getBlockManagerClassName() {
    return fBlockManagerClassName;
  }

  public int getFileLayoutVersion() {
    return fFileLayoutVersion;
  }

  public int getHeaderSize() {
    return fHeaderSize;
  }

  public UUID getHostUuid() {
    return fHostUuid;
  }

  public String getIdFactoryClassName() {
    return fIdFactoryClassName;
  }

  public UUID getThisUuid() {
    return fThisUuid;
  }

  public Version getVersion() {
    return fVersion;
  }

  public Object loadBlockManager() {
    return ReflectionUtil.createInstance(getBlockManagerClassName());
  }

  public IObjectIdFactory loadIdFactory() {
    return ReflectionUtil.createInstance(getIdFactoryClassName());
  }
  
  
}
