package org.memoriadb.core.load.binder;

import org.memoriadb.core.load.*;
import org.memoriadb.handler.array.IArray;
import org.memoriadb.id.IObjectId;

public class BindArray implements IBindable {
  
  private final IObjectId fId;
  private final IArray fArray;
  private final int fIndex;

  public BindArray(IArray array, int index, IObjectId objectId) {
    fIndex = index;
    fArray = array;
    fId = objectId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
      Object obj = context.isNullReference(fId)? null: context.getExistingObject(fId);
      fArray.set(fIndex, obj);
  }

}
