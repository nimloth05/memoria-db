package org.memoriadb.core.load;

import org.memoriadb.core.meta.*;

public class ClassInheritanceBinder implements IBindable {

  private final MetaClass fSubClass;
  private final long fSuperClassId;

  public ClassInheritanceBinder(MetaClass classObject, long superClassId) {
    fSubClass = classObject;
    fSuperClassId = superClassId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fSubClass.setSuperClass((IMetaClass) context.getObjectById(fSuperClassId));
  }

}
