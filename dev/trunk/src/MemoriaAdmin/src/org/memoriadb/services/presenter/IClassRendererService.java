package org.memoriadb.services.presenter;

import org.memoriadb.core.meta.IMemoriaClass;

public interface IClassRendererService {

  public IClassRenderer getRednerer(IMemoriaClass memoriaClass);

}
