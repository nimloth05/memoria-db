/**
 * 
 */
package org.memoriadb.core.meta;

import org.memoriadb.core.load.IReaderContext;

public class TypeVisitorHelper<T, M> implements ITypeVisitor {
  
  public M fMember;
  public T fResult;
  public IReaderContext fContext;

  public TypeVisitorHelper(M member, IReaderContext context) {
    fMember = member;
    fContext = context;
  }
  
  @Override
  public void visitClass(Type type, long objectId) {}

  @Override
  public void visitPrimitive(Type type, Object value) {}
  
}