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

/**
 * 
 */
package org.memoriadb.handler.map;

import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.id.IObjectId;

class ReferenceResolver implements IObjectResolver {

  private final IObjectId fId;

  public ReferenceResolver(IObjectId id) {
    fId = id;
  }

  @Override
  public Object getObject(IReaderContext context) {
    return context.getExistingObject(fId);
  }

}