/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.core;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.Constants;
import org.memoriadb.id.IObjectId;

/**
 * Holds all data to an object necessary for internal housekeeping
 * 
 * fObj == null means, that this object has been deleted
 * 
 * @author msc
 * 
 */
public class ObjectInfo implements IObjectInfo {
  
  /**
   *  null for deleted object
   */
  private Object fObj;
  private final IObjectId fId;
  private final IObjectId fMemoriaClassId;
  // TODO kann zum Block verschoben werden
  private long fRevision;
  private int fOldGenerationCount;
  
  /**
   * Use this ctor only when an object is initially added to the container.
   * @param id
   * @param memoriaClassId
   * @param obj
   * @param currentBlock
   */
  public ObjectInfo(IObjectId id, IObjectId memoriaClassId, Object obj) {
    this(id, memoriaClassId, obj, Constants.INITIAL_HEAD_REVISION, 0);
    if(obj == null) throw new MemoriaException("new object can not be null");
  }

  /**
   * Use this ctor for ojects after dehydration
   * @param id
   * @param memoriaClassId
   * @param obj
   * @param currentBlock
   * @param version
   * @param oldGenerationCount
   */
  public ObjectInfo(IObjectId id, IObjectId memoriaClassId, Object obj, long version, int oldGenerationCount) {
    if (memoriaClassId == null) throw new IllegalArgumentException("MemoriaClassId is null.");
    
    fObj = obj;
    
    fId = id;
    fMemoriaClassId = memoriaClassId;
    fRevision = version;
    fOldGenerationCount = oldGenerationCount;
  }

  public void decrementOldGenerationCount() {
    --fOldGenerationCount;
    
    if(fOldGenerationCount < 0) throw new MemoriaException("invalid oldgenerationCount: " + fOldGenerationCount);
  }
  
  @Override
  public IObjectId getId(){
    return fId;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  @Override
  public Object getObject() {
    return fObj;
  }

  @Override
  public int getOldGenerationCount() {
    return fOldGenerationCount;
  }

  @Override
  public long getRevision() {
    return fRevision;
  }

  public void incrementOldGenerationCount() {
    ++fOldGenerationCount;
  }

  @Override
  public boolean isDeleted() {
    return fObj == null;
  }
  
  public void setDeleted() {
    fObj = null;
  }
  
  public void setObj(Object obj) {
    fObj = obj;
  }
  
  public void setRevision(long revision) {
    fRevision = revision;
  }

  @Override
  public String toString() {
    if (isDeleted()) return fId + " DELETED rev " + fRevision; 
    return fId + " " + fObj.getClass().getName() + " rev " + fRevision;
  }
  
}
