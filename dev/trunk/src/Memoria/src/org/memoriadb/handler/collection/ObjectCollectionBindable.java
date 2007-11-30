package org.memoriadb.handler.collection;

import java.util.Collection;

import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.handler.IBindable;
import org.memoriadb.id.IObjectId;

public class ObjectCollectionBindable implements IBindable {
  private final Collection<Object> fResult;
  private final IObjectId fObjectId;

  public ObjectCollectionBindable(Collection<Object> result, IObjectId objectId) {
    fResult = result;
    fObjectId = objectId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fResult.add(context.getExistingObject(fObjectId));
  }

  @Override
  public String toString() {
    return "list: " + fResult + " object to add: " + fObjectId;
  }

}
