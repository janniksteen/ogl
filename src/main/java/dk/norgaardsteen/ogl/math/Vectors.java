package dk.norgaardsteen.ogl.math;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: jns
 * Date: 1/6/14
 * Time: 7:33 PM
 */
public class Vectors {

  /* axis vectors */
  public static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
  public static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
  public static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

  private static final Vector3f NORMAL = new Vector3f(0.0f, 0.0f, 1.0f);
  private static final Vector3f INITIAL_VERTEX = new Vector3f(-1.0f, 1.0f, 0.0f);

  /*
   * @return a list of vectors in counter clockwise direction
   * defining a quadratic face with a normal as (0.0, 0.0, 1.0)
   */
  public static Collection<Vector3f> createFace() {
    Collection<Vector3f> vectors = new ArrayList<>();
    return internalCreateFace(vectors, NORMAL, INITIAL_VERTEX);
  }

  private static Collection<Vector3f> internalCreateFace(Collection<Vector3f> vectors, Vector3f normal, Vector3f vertex) {
    while (vectors.size() < 4) {
      Vector3f v = nextVertex(normal, vertex);
      vectors.add(v);
      internalCreateFace(vectors, normal, v);
    }
    return vectors;
  }

  /*
   * Given a face normal and a initial vertex this method will return the
   * next vertex of a quadratic face in a counter clockwise direction.
   *
   * see: http://math.stackexchange.com/questions/304700/how-to-generate-an-ordered-list-of-vertices-of-a-cube-from-a-face-and-a-normal-v
   */
  private static Vector3f nextVertex(Vector3f normal, Vector3f vertex) {
    Vector3f next = new Vector3f();
    Vector3f.cross(normal, vertex, next);
    Vector3f.add(normal, next, next);
    return next;
  }

}
