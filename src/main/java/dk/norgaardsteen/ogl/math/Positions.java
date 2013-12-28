package dk.norgaardsteen.ogl.math;

/**
 * User: jns
 * Date: 12/18/13
 * Time: 12:25 PM
 */
public class Positions {

  /**
   * Translate x and y to homogeneous positions
   */
  public static float[] translateToClipSpace(float x, float y, float screenWidth, float screenHeight) {
    float homogeneousXPosition = (x - (screenWidth / 2)) / screenWidth;
    float homogeneousYPosition = (y - (screenHeight / 2)) / screenHeight;
    return new float[] {homogeneousXPosition, homogeneousYPosition};
  }
}
