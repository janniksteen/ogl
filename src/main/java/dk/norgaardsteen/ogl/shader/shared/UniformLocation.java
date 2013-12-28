package dk.norgaardsteen.ogl.shader.shared;

/**
 * User: jns
 * Date: 11/28/13
 * Time: 8:43 PM
 */
public class UniformLocation {

  public static final String PROJECTION_MATRIX_UNIFORM_LOCATION_NAME = "projectionMatrix";
  public static final String VIEW_MATRIX_UNIFORM_LOCATION_NAME = "viewMatrix";
  public static final String MODEL_MATRIX_UNIFORM_LOCATION_NAME = "modelMatrix";

  public static final String DISPLAY_WIDTH_UNIFORM_LOCATION_NAME = "displayWidth";
  public static final String DISPLAY_HEIGHT_UNIFORM_LOCATION_NAME = "displayHeight";

  public int handle = -1;
  public String name = null;

  public UniformLocation(String name) {
    this.name = name;
  }
}
