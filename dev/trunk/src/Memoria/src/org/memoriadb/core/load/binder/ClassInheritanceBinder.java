package org.memoriadb.core.load.binder;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.*;
import org.memoriadb.core.meta.*;

public class ClassInheritanceBinder implements IBindable {

  private final FieldbasedMemoriaClass fSubClass;
  private final IObjectId fSuperClassId;

  public ClassInheritanceBinder(FieldbasedMemoriaClass classObject, IObjectId superClassId) {
    fSubClass = classObject;
    fSuperClassId = superClassId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fSubClass.setSuperClass((IMemoriaClass) context.getExistingObject(fSuperClassId));
  }

}
