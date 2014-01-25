package dk.norgaardsteen.ogl.math;

import org.lwjgl.util.vector.Vector3f;

/**
 * User: jns
 * Date: 1/6/14
 * Time: 7:33 PM
 */
public class Vectors {
  /*
   * see: http://math.stackexchange.com/questions/304700/how-to-generate-an-ordered-list-of-vertices-of-a-cube-from-a-face-and-a-normal-v
   */
  private Vector3f nextVertex(Vector3f normal, Vector3f vertex) {
    Vector3f next = new Vector3f();
    Vector3f.cross(normal, vertex, next);
    Vector3f.add(normal, next, next);
    return next;
  }

}
