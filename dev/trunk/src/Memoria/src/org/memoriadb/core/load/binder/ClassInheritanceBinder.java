package org.memoriadb.core.load.binder;

import org.memoriadb.core.load.*;
import org.memoriadb.core.meta.*;

public class ClassInheritanceBinder implements IBindable {

  private final MemoriaFieldClass fSubClass;
  private final long fSuperClassId;

  public ClassInheritanceBinder(MemoriaFieldClass classObject, long superClassId) {
    fSubClass = classObject;
    fSuperClassId = superClassId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fSubClass.setSuperClass((IMetaClass) context.getObjectById(fSuperClassId));
  }

}
