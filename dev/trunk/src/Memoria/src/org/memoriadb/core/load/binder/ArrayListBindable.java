package org.memoriadb.core.load.binder;

import java.util.ArrayList;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.*;


public class ArrayListBindable implements IBindable {

  private final ArrayList<Object> fResult;
  private final IObjectId fObjectId;

  public ArrayListBindable(ArrayList<Object> result, IObjectId objectId) {
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
