package org.memoriadb.core.handler.def;
// FIXME kann entfernt werden, abgelöst durch Type, msc
public enum ArrayComponentType {

  booleanPrimitive {

    @Override
    public Class<?> getJavaClass() {
      return boolean.class;
    }
  },

  booleanClass {

    @Override
    public Class<?> getJavaClass() {
      return Boolean.class;
    }
  },

  charPrimitive {

    @Override
    public Class<?> getJavaClass() {
      return char.class;
    }
  },

  charClass {

    @Override
    public Class<?> getJavaClass() {
      return Character.class;
    }
  },

  bytePrimitive {

    @Override
    public Class<?> getJavaClass() {
      return byte.class;
    }
  },

  byteClass {

    @Override
    public Class<?> getJavaClass() {
      return Byte.class;
    }
  },

  shortPrimitive {

    @Override
    public Class<?> getJavaClass() {
      return short.class;
    }
  },

  shortClass {

    @Override
    public Class<?> getJavaClass() {
      return Short.class;
    }
  },

  integerPrimitive {

    @Override
    public Class<?> getJavaClass() {
      return int.class;
    }
  },

  integerClass {

    @Override
    public Class<?> getJavaClass() {
      return Integer.class;
    }
  },

  longPrimitive {

    @Override
    public Class<?> getJavaClass() {
      return long.class;
    }
  },

  longClass {

    @Override
    public Class<?> getJavaClass() {
      return Long.class;
    }
  },

  floatPrimitive {
    @Override
    public Class<?> getJavaClass() {
      return float.class;
    }
  },

  floatClass {

    @Override
    public Class<?> getJavaClass() {
      return Float.class;
    }
  },

  doublePrimitive {

    @Override
    public Class<?> getJavaClass() {
      return double.class;
    }
  },

  doubleClass {

    @Override
    public Class<?> getJavaClass() {
      return Double.class;
    }
  },

  stringClass {

    @Override
    public Class<?> getJavaClass() {
      return String.class;
    }
  };

  public static ArrayComponentType get(Class<?> type) {
    if (type.equals(boolean.class)) return booleanPrimitive;
    if (type.equals(Boolean.class)) return booleanClass;
    
    if (type.equals(char.class)) return charPrimitive;
    if (type.equals(Character.class)) return charClass;
    
    if (type.equals(byte.class)) return bytePrimitive;
    if (type.equals(Byte.class)) return byteClass;
    
    if (type.equals(short.class)) return shortPrimitive;
    if (type.equals(Short.class)) return shortClass;
    
    if (type.equals(int.class)) return integerPrimitive;
    if (type.equals(Integer.class)) return integerClass;
    
    if (type.equals(long.class)) return longPrimitive;
    if (type.equals(Long.class)) return longClass;
    
    if (type.equals(float.class)) return floatPrimitive;
    if (type.equals(Float.class)) return floatClass;
    
    if (type.equals(double.class)) return doublePrimitive;
    if (type.equals(Double.class)) return doubleClass;
    
    if (type.equals(String.class)) return stringClass;
    
    return null;
  }

  public abstract Class<?> getJavaClass();

}
