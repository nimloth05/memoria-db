package org.memoriadb.core.file;

import java.util.UUID;

import org.memoriadb.core.IDefaultInstantiator;
import org.memoriadb.core.id.IObjectIdFactory;
import org.memoriadb.util.*;


/**
 * Holds the information stored in the memoria file-header 
 * @author msc
 */
public class FileHeader {

  private final UUID fThisUuid;
  private final UUID fHostUuid;
  
  // Only set when this is a client-db. Host revision at the time when this db was copied.
  private final long fHostBranchRevision;
  private final Version fVersion;
  private final int fFileLayoutVersion;
  private final String fIdFactoryClassName;
  private final int fHeaderSize;
  private final String fDefaultInstantiatorClassName;
  private final LastWrittenBlockInfo fLastWrittenBlockInfo;

  public FileHeader(UUID thisUuid, UUID hostUuid, long hostBranchRevision, Version version, int fileLayoutVersion, String idFactoryClassName, String defaultInstantiator, int headerSize, LastWrittenBlockInfo lastWrittenBlockInfo) {
    fThisUuid = thisUuid;
    fHostUuid = hostUuid;
    fHostBranchRevision = hostBranchRevision;
    fVersion = version;
    fFileLayoutVersion = fileLayoutVersion;
    fIdFactoryClassName = idFactoryClassName;
    fDefaultInstantiatorClassName = defaultInstantiator;
    fHeaderSize = headerSize;
    fLastWrittenBlockInfo = lastWrittenBlockInfo;
  }

  public int getFileLayoutVersion() {
    return fFileLayoutVersion;
  }

  public int getHeaderSize() {
    return fHeaderSize;
  }

  public long getHostBranchRevision() {
    return fHostBranchRevision;
  }

  public UUID getHostUuid() {
    return fHostUuid;
  }

  public String getIdFactoryClassName() {
    return fIdFactoryClassName;
  }

  public LastWrittenBlockInfo getLastWrittenBlockInfo() {
    return fLastWrittenBlockInfo;
  }

  public UUID getThisUuid() {
    return fThisUuid;
  }

  public Version getVersion() {
    return fVersion;
  }

  public IDefaultInstantiator loadDefaultInstantiator() {
    return ReflectionUtil.createInstance(getDefaultInstantiatorClassName());
  }

  public IObjectIdFactory loadIdFactory() {
    return ReflectionUtil.createInstance(getIdFactoryClassName());
  }

  private String getDefaultInstantiatorClassName() {
    return fDefaultInstantiatorClassName;
  }
  
}
