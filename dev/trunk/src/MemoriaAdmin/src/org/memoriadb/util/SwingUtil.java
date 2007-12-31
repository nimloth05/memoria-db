package org.memoriadb.util;

import java.awt.*;

public final class SwingUtil {
  
  public static Point calculateCenter(Dimension size) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    return new Point((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 2); 
  }
  
  public static Point relativeTo(Window frame, int x, int y) {
    Point point = (Point) frame.getLocation().clone();
    point.translate(x, y);
    return point;
  }

  private SwingUtil() {}

}
