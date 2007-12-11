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