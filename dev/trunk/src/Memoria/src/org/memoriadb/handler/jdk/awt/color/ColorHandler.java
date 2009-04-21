package org.memoriadb.handler.jdk.awt.color;

import java.awt.*;
import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.jdk.JDKDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public class ColorHandler implements IHandler {
  
  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!getClassName().equals(className)) throw new SchemaException("I am a handler for type " + getClassName() + " but I was called for " + className);
  }
  
  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws Exception {
     Color color = deserializeColor(input);
    
    return !context.isInDataMode() ? color : JDKDataObject.create(typeId, color);
  }
  
  @Override
  public String getClassName() {
    return Color.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    Color color = getColorObject(obj);
    serializeColorComponents(color, output, context);
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {}
  
  private Color deserializeColor(DataInputStream input) throws IOException {
    int value = input.readInt();
    boolean hasAlpha = input.readBoolean();
    return new Color(value, hasAlpha);
  }

  @SuppressWarnings("unchecked")
  private Color getColorObject(Object obj) {
    if (obj instanceof JDKDataObject<?>) return ((JDKDataObject<Color>)obj).getObject();
    return (Color) obj;
  }

  private boolean hasAlpha(Color color) {
    return color.getTransparency() != Transparency.OPAQUE;
  }

  private void serializeColorComponents(Color color, DataOutput output, IWriterContext context) throws Exception {
    output.writeInt(color.getRGB());
    output.writeBoolean(hasAlpha(color));
  }

}
