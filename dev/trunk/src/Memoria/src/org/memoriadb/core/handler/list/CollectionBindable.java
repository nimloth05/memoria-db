package org.memoriadb.core.handler.list;

import java.util.Collection;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.*;


public class CollectionBindable implements IBindable {

  private final Collection<Object> fResult;
  private final IObjectId fObjectId;

  public CollectionBindable(Collection<Object> result, IObjectId objectId) {
    fResult = result;
    fObjectId = objectId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fResult.add(context.getObjectById(fObjectId));
  }

  @Override
  public String toString() {
    return "list: "+ fResult + " object to add: " + fObjectId;
  }

}
