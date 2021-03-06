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

package org.memoriadb.core;

import org.memoriadb.id.IObjectId;

/**
 * Read-only interface. 
 * {@link ObjectInfo}s are part of Memoria's internal data structure and must not be changed! 
 *
 * @author Sandro
 * @author msc
 *
 */
public interface IObjectInfo {

  public IObjectId getId();

  public IObjectId getMemoriaClassId();

  public Object getObject();

  public int getOldGenerationCount();

  public boolean isDeleted();

}
