package org.memoriadb.handler.collection;

import java.util.Collection;

import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.IBindable;

public class CollectionBindable implements IBindable {

  private final Collection<Object> fCollection;
  private final Object fValue;

  public CollectionBindable(Collection<Object> collection, Object value) {
    fCollection = collection;
    fValue = value;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fCollection.add(fValue);
  }

}
