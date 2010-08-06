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

package org.memoriadb.core.file.read;

import org.memoriadb.block.Block;
import org.memoriadb.id.IObjectId;

/**
 *
 * This handler is called during the read-process of the {@link FileReader}
 * 
 * @author Sandro
 *
 */
public interface IFileReaderHandler {
  
  public void block(Block block);

  public void memoriaClass(HydratedObject metaClass, IObjectId id, int size);
  public void memoriaClassDeleted(IObjectId id);
  
  public void object(HydratedObject object, IObjectId id, int size);
  public void objectDeleted(IObjectId id);
  
}
