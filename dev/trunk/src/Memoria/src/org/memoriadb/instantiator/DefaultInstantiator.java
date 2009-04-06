package org.memoriadb.instantiator;

import org.memoriadb.core.util.ReflectionUtil;

public class DefaultInstantiator implements IInstantiator {

  public void checkCanInstantiateObject(String className) throws CannotInstantiateException {
    try {
      Class<?> clazz = ReflectionUtil.getClassUnsave(className);
      clazz.getDeclaredConstructor();
    }
    catch(ClassNotFoundException e) {
      throw new CannotInstantiateException("Problem during class loading: " + className, e);
    }
    catch(NoSuchMethodException e) {
      throw new CannotInstantiateException("Default constructor not found: " + className, e); 
    }
    catch(SecurityException e) {
      throw new CannotInstantiateException("Security Problem: " + className, e);
    }
  }

  @Override
  public <T> T newInstance(String className) {
    return ReflectionUtil.<T>createInstance(className);
  }

}
