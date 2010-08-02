/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.handler.jdk.awt.color;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.jdk.JDKDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

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
