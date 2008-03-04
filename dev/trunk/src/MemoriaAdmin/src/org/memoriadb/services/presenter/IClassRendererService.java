package org.memoriadb.services.presenter;

import org.memoriadb.core.meta.IMemoriaClass;

import com.google.inject.ImplementedBy;

@ImplementedBy(ClassRendererService.class)
public interface IClassRendererService {

  public IClassRenderer getRednerer(IMemoriaClass memoriaClass);

}
