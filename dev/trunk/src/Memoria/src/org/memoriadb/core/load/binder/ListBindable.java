package org.memoriadb.core.load.binder;

import java.util.List;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.*;


public class ListBindable implements IBindable {

  private final List<Object> fResult;
  private final IObjectId fObjectId;

  public ListBindable(List<Object> result, IObjectId objectId) {
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
