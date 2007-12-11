/**
 * 
 */
package org.memoriadb.handler.map;

import org.memoriadb.core.file.read.IReaderContext;

interface IObjectResolver {
  public Object getObject(IReaderContext context);
}