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

package org.memoriadb.core.file;

import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.core.util.Version;
import org.memoriadb.id.IObjectIdFactory;
import org.memoriadb.instantiator.IInstantiator;

import java.util.UUID;


/**
 * Holds the information stored in the memoria file-header
 *  
 * @author msc
 */
public class Header {

  private final UUID fThisUuid;
  private final UUID fHostUuid;
  
  // Only set when this is a client-db. Host revision at the time when this db was copied.
  private final long fHostBranchRevision;
  private final Version fVersion;
  private final int fFileLayoutVersion;
  private final String fIdFactoryClassName;
  private final int fHeaderSize;
  private final IInstantiator fInstantiator;
  private final LastWrittenBlockInfo fLastWrittenBlockInfo;
  private final ICompressor fCompressor;

  public Header(UUID thisUuid, UUID hostUuid, long hostBranchRevision, Version version, int fileLayoutVersion, String idFactoryClassName, String defaultInstantiator, int headerSize, LastWrittenBlockInfo lastWrittenBlockInfo, ICompressor compressor) {
    fThisUuid = thisUuid;
    fHostUuid = hostUuid;
    fHostBranchRevision = hostBranchRevision;
    fVersion = version;
    fFileLayoutVersion = fileLayoutVersion;
    fIdFactoryClassName = idFactoryClassName;
    fInstantiator = loadDefaultInstantiator(defaultInstantiator);
    fHeaderSize = headerSize;
    fLastWrittenBlockInfo = lastWrittenBlockInfo;
    fCompressor = compressor;
  }

  public ICompressor getCompressor() {
    return fCompressor; 
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

  public IInstantiator getInstantiator() {
    return fInstantiator;
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
  
  public IObjectIdFactory loadIdFactory() {
    return ReflectionUtil.createInstance(getIdFactoryClassName());
  }

  private IInstantiator loadDefaultInstantiator(String className) {
    return ReflectionUtil.createInstance(className);
  }
  
}
