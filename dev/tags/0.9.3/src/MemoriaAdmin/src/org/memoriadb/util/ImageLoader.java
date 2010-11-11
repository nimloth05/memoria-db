/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public final class ImageLoader {
  
  /**
   * This method returns always an image.
   * @param path
   * @return
   */
  public static ImageIcon loadImageIcon(String path) {
    return new ImageIcon(loadIconImage(path));
  }
  
  private static BufferedImage createSingleColorImage(int width, int heigth) {
    BufferedImage errorImage = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = errorImage.getGraphics();
    graphics.setColor(Color.red);
    graphics.fillRect(0, 0, width, heigth);
    graphics.dispose();
    return errorImage;
  }

  private static BufferedImage loadIconImage(String path) {
    try {
      return ImageIO.read(ImageLoader.class.getResource(path));
    }
    catch (Exception e) {
      return createSingleColorImage(16, 16);
    }
  }
  
  private ImageLoader() {}

}
