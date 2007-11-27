package org.memoriadb.core;

import org.memoriadb.util.ReflectionUtil;

public class DefaultInstantiator implements IInstantiator {

  public boolean canInstantiateObject(String className) {
    return ReflectionUtil.hasNoArgCtor(className);
  }

  @Override
  public <T> T newInstance(String className) {
    return ReflectionUtil.createInstance(className);
  }

}