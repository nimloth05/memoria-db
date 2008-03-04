package org.memoriadb.util;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public final class ImageLoader {
  
  /**
   * This method returns always an image.
   * @param path
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
