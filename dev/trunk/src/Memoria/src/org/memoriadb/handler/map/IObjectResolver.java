/**
 * 
 */
package org.memoriadb.handler.map;

import org.memoriadb.core.load.IReaderContext;

interface IObjectResolver {
  public Object getObject(IReaderContext context);
}