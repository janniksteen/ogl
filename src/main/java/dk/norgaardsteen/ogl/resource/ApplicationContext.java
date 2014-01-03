package dk.norgaardsteen.ogl.resource;

import org.lwjgl.opengl.Display;

/**
 * User: jns
 * Date: 12/30/13
 * Time: 3:59 PM
 */
public class ApplicationContext {
  public static int getDisplayXSize() {
    return Display.getWidth();
  }

  public static int getDisplayYSize() {
    return Display.getHeight();
  }
}
