package org.memoriadb.core.load;

import java.util.ArrayList;


public class ArrayListBindable implements IBindable {

  private final ArrayList<Object> fResult;
  private final long fObjectId;

  public ArrayListBindable(ArrayList<Object> result, long objectId) {
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
