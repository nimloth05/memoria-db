package org.memoriadb.handler.field;

import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.handler.IBindable;
import org.memoriadb.id.IObjectId;

public class ClassInheritanceBinding implements IBindable {

  private final IMemoriaClassConfig fSubClass;
  private final IObjectId fSuperClassId;

  public ClassInheritanceBinding(IMemoriaClassConfig classObject, IObjectId superClassId) {
    fSubClass = classObject;
    fSuperClassId = superClassId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fSubClass.setSuperClass((IMemoriaClass) context.getExistingObject(fSuperClassId));
  }

}
