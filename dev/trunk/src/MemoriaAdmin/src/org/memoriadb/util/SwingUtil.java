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
