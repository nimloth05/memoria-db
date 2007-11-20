package org.memoriadb.core.meta;

import org.memoriadb.core.id.IObjectId;


public interface ITypeVisitor {
  
  public class Adapter implements ITypeVisitor {

    @Override
    public void visitClass(Type type, IObjectId objectId) {}
    
    @Override
    public void visitEnum(Type type, int enumOrdinal) {}

    @Override
    public void visitPrimitive(Type type, Object value) {}

  }
  
  public void visitClass(Type type, IObjectId objectId);
  public void visitEnum(Type type, int enumOrdinal);
  public void visitPrimitive(Type type, Object value);
  
   

}
