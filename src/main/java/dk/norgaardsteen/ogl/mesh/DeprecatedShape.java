package dk.norgaardsteen.ogl.mesh;

/**
 * User: jns
 * Date: 11/19/13
 * Time: 4:41 PM
 */
public interface DeprecatedShape {

  public abstract float[] vertices();

  /**
   * @return the number of position elements of each vertex, e.g. 3 or 4 provided by {1,0,1} or {0.5,-1,0,1}
   */
  public abstract int vertexPositionElementCount();

  public abstract float[] colors();
}