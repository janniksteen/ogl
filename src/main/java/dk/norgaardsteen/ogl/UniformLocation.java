package dk.norgaardsteen.ogl;

/**
 * User: jns
 * Date: 11/28/13
 * Time: 8:43 PM
 */
public class UniformLocation {

  public static final String PROJECTION_MATRIX_UNIFORM_LOCATION_NAME = "projectionMatrix";
  public static final String VIEW_MATRIX_UNIFORM_LOCATION_NAME = "viewMatrix";
  public static final String MODEL_MATRIX_UNIFORM_LOCATION_NAME = "modelMatrix";

  public int handle = -1;
  public String name = null;

  public UniformLocation(String name) {
    this.name = name;
  }
}
