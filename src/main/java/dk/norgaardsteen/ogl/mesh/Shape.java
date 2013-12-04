package dk.norgaardsteen.ogl.mesh;

/**
 * User: jns
 * Date: 11/19/13
 * Time: 4:41 PM
 */
public interface Shape {
  public abstract float[] vertices();

  /**
   * @return the number of position elements of each vertex, e.g. 3 or 4 provided by {1,0,1} or {0.5,-1,0,1}
   */
  public abstract int vertexPositionElementCount();

  /**
   * @return the number of triangles that comprises this shape
   */
  public abstract int numberOfTriangles();

  public abstract int vertexPositionElementOffset();

  public abstract float[] colors();
  /**
    *
    * @return the number of elements of each color, e.g. 3 provided by {0.545, 0.000, 0.000}
   */
  public abstract int colorDefinitionElementCount();
  public abstract int colorDefinitionElementOffset();

  public abstract float[] textures();
  /**
   *
   * @return the number of elements of each texture
   */
  public abstract int textureElementCount();
}