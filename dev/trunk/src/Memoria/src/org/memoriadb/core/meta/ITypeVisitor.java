package org.memoriadb.core.meta;


public interface ITypeVisitor {
  
  public class Adapter implements ITypeVisitor {

    @Override
    public void visitClass(Type type, long objectId) {}
    
    @Override
    public void visitPrimitive(Type type, Object value) {}

  }
  
  public void visitClass(Type type, long objectId);
  public void visitPrimitive(Type type, Object value);
  
   

}
