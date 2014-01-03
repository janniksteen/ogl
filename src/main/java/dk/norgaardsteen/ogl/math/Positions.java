package dk.norgaardsteen.ogl.math;

import org.lwjgl.util.vector.Vector2f;

/**
 * User: jns
 * Date: 12/18/13
 * Time: 12:25 PM
 */
public class Positions {

  /**
   * Translate x and y to homogeneous positions
   */
  public static Vector2f translateToClipSpace(float x, float y, float screenWidth, float screenHeight) {
    float homogeneousXPosition = (x - (screenWidth / 2)) / (screenWidth / 2);
    float homogeneousYPosition = (y - (screenHeight / 2)) / (screenHeight / 2);
    return new Vector2f(homogeneousXPosition, homogeneousYPosition);
  }
}
