package org.memoriadb.instantiator;

import org.memoriadb.core.util.ReflectionUtil;

public class DefaultInstantiator implements IInstantiator {

  public boolean canInstantiateObject(String className) {
    return ReflectionUtil.hasNoArgCtor(className);
  }

  @Override
  public <T> T newInstance(String className) {
    return ReflectionUtil.createInstance(className);
  }

}
