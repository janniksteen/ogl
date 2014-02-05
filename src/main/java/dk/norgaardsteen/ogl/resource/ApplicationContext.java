package dk.norgaardsteen.ogl.resource;

import org.lwjgl.opengl.Display;

import java.util.Random;

/**
 * User: jns
 * Date: 12/30/13
 * Time: 3:59 PM
 */
public class ApplicationContext {
  private static Random random = new Random(42);

  public static float getRandomFloat() {
    return random.nextFloat();
  }

  public static int getRandomInt() {
    return random.nextInt();
  }

  public static int getRandomInt(int max) {
    return random.nextInt(max);
  }

  public static int getDisplayXSize() {
    return Display.getWidth();
  }

  public static int getDisplayYSize() {
    return Display.getHeight();
  }
}
