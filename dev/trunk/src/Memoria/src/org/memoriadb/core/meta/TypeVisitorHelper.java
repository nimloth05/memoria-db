/**
 * 
 */
package org.memoriadb.core.meta;

import org.memoriadb.core.load.IReaderContext;

public abstract class TypeVisitorHelper<R, M> implements ITypeVisitor {
  
  public R fResult;
  public M fMember;
  public IReaderContext fContext;

  public TypeVisitorHelper(IReaderContext context) {
    fContext = context;
  }
  
  public TypeVisitorHelper(M member, IReaderContext context) {
    fMember = member;
    fContext = context;
  }
  
  public void setMember(M value) {
    fMember = value;
  }
  
  public void setResult(R value) {
    fResult = value;
  }

  @Override
  public void visitClass(Type type, long objectId) {}
  
  @Override
  public void visitPrimitive(Type type, Object value) {}
  
}