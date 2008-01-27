package org.memoriadb.core.meta;

import org.memoriadb.id.IObjectId;


public interface ITypeVisitor {
  
  public class Adapter implements ITypeVisitor {

    @Override
    public void visitClass(Type type, IObjectId objectId) {}
    
    @Override
    public void visitNull() {}

    @Override
    public void visitPrimitive(Type type, Object value) {}

    @Override
    public void visitValueObject(Object value) {}
    
  }
  
  public void visitClass(Type type, IObjectId objectId);
  public void visitNull();
  public void visitPrimitive(Type type, Object value);
  public void visitValueObject(Object value);
  
   

}
