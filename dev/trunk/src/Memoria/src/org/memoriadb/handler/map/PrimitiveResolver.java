/**
 * 
 */
package org.memoriadb.handler.map;

import org.memoriadb.core.file.read.IReaderContext;

class PrimitiveResolver implements IObjectResolver {

  private final Object fObject;

  public PrimitiveResolver(Object object) {
    fObject = object;
  }

  @Override
  public Object getObject(IReaderContext context) {
    return fObject;
  }

}