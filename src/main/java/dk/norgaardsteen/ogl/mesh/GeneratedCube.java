package dk.norgaardsteen.ogl.mesh;

import dk.norgaardsteen.ogl.mesh.color.ColorCollection;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * User: jns
 * Date: 1/5/14
 * Time: 5:00 PM
 */
public class GeneratedCube extends QuadShape implements Shape {

  // axis vectors
  private static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
  private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
  private static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

  // matrices
  private static final Matrix4f LEFT_FACE;
  private static final Matrix4f RIGHT_FACE;
  private static final Matrix4f FRONT_FACE;
  private static final Matrix4f BACK_FACE;
  private static final Matrix4f TOP_FACE;
  private static final Matrix4f BOTTOM_FACE;

  static {
    FRONT_FACE = new Matrix4f();
    Matrix4f.translate(new Vector3f(0.0f, 0.0f, 8.0f), FRONT_FACE, FRONT_FACE);
    Matrix4f.rotate((float)Math.toRadians(0), Z_AXIS, FRONT_FACE, FRONT_FACE);
    Matrix4f.rotate((float)Math.toRadians(0), Y_AXIS, FRONT_FACE, FRONT_FACE);
    Matrix4f.rotate((float)Math.toRadians(0), X_AXIS, FRONT_FACE, FRONT_FACE);
  }

  static {
    LEFT_FACE = new Matrix4f();
    Matrix4f.translate(new Vector3f(-8.0f, 0.0f, 0.0f), LEFT_FACE, LEFT_FACE);
    Matrix4f.rotate((float)Math.toRadians(0), Z_AXIS, LEFT_FACE, LEFT_FACE);
    Matrix4f.rotate((float)Math.toRadians(-90), Y_AXIS, LEFT_FACE, LEFT_FACE);
    Matrix4f.rotate((float) Math.toRadians(0), X_AXIS, LEFT_FACE, LEFT_FACE);
  }

  static {
    RIGHT_FACE = new Matrix4f();
    Matrix4f.translate(new Vector3f(8.0f, 0.0f, 0.0f), RIGHT_FACE, RIGHT_FACE);
    Matrix4f.rotate((float)Math.toRadians(0), Z_AXIS, RIGHT_FACE, RIGHT_FACE);
    Matrix4f.rotate((float)Math.toRadians(-270), Y_AXIS, RIGHT_FACE, RIGHT_FACE);
    Matrix4f.rotate((float) Math.toRadians(0), X_AXIS, RIGHT_FACE, RIGHT_FACE);
  }

  static {
    BACK_FACE = new Matrix4f();
    Matrix4f.translate(new Vector3f(0.0f, 0.0f, -8.0f), BACK_FACE, BACK_FACE);
    Matrix4f.rotate((float)Math.toRadians(0), Z_AXIS, BACK_FACE, BACK_FACE);
    Matrix4f.rotate((float)Math.toRadians(-180), Y_AXIS, BACK_FACE, BACK_FACE);
    Matrix4f.rotate((float) Math.toRadians(0), X_AXIS, BACK_FACE, BACK_FACE);
  }

  static {
    TOP_FACE = new Matrix4f();
    Matrix4f.translate(new Vector3f(0.0f, 8.0f, 0.0f), TOP_FACE, TOP_FACE);
    Matrix4f.rotate((float)Math.toRadians(0), Z_AXIS, TOP_FACE, TOP_FACE);
    Matrix4f.rotate((float)Math.toRadians(0), Y_AXIS, TOP_FACE, TOP_FACE);
    Matrix4f.rotate((float)Math.toRadians(270), X_AXIS, TOP_FACE, TOP_FACE);
  }

  static {
    BOTTOM_FACE = new Matrix4f();
    Matrix4f.translate(new Vector3f(0.0f, -8.0f, 0.0f), BOTTOM_FACE, BOTTOM_FACE);
    Matrix4f.rotate((float)Math.toRadians(0), Z_AXIS, BOTTOM_FACE, BOTTOM_FACE);
    Matrix4f.rotate((float)Math.toRadians(0), Y_AXIS, BOTTOM_FACE, BOTTOM_FACE);
    Matrix4f.rotate((float)Math.toRadians(-270), X_AXIS, BOTTOM_FACE, BOTTOM_FACE);
  }

  private static final Matrix4f[] FACES = {
    FRONT_FACE,
    BACK_FACE,
    LEFT_FACE,
    RIGHT_FACE,
    TOP_FACE,
    BOTTOM_FACE
  };

  private static final List<Vector4f> fragmentVertices= new ArrayList<>();

  static {
    float dimension = 16.0f;
    float xpos = -dimension / 2;
    float ypos = -dimension / 2;
    for (int y = 0; y < dimension; y++) {
      for (int x = 0; x < dimension; x++) {
        fragmentVertices.add(new Vector4f(xpos, ypos, 0.0f, 1.0f));
        fragmentVertices.add(new Vector4f(xpos + 1.0f, ypos, 0.0f, 1.0f));
        fragmentVertices.add(new Vector4f(xpos + 1.0f, ypos + 1.0f, 0.0f, 1.0f));
        fragmentVertices.add(new Vector4f(xpos, ypos + 1.0f, 0.0f, 1.0f));
        xpos += 1.0f;
      }
      xpos = -dimension / 2;
      ypos += 1.0f;
    }
  }

  private Random random = new Random(42);
  private Collection<Vertex> vertices;

  public static void main(String[] args) {
    new GeneratedCube();
  }

  public GeneratedCube() {
    this.vertices = new ArrayList<>();
    generateCube2(ColorCollection.DIRT_GARDEN);
  }

  private void generateCube2(float[] colors) {
    for (int face = 0; face < FACES.length; face++) {
      int c = 0;
      int fragmentIdx = 0;
      Collection<Vector4f> fragments = getFragmentVertices();
      for (Vector4f v : fragments) {
        Matrix4f.transform(FACES[face], v, v);
        if (fragmentIdx % 4 == 0) {
          c = random.nextInt(colors.length / 3);
        }
        Vertex vertex = new Vertex().setXYZW(v.x, v.y, v.z, v.w).setRGB(colors[c * 3], colors[(c * 3) + 1], colors[(c * 3) + 2]);
        fragmentIdx++;
        vertices.add(vertex);
      }
    }
    int offset = 0;
    indices = new short[(vertices.size() / 4) * 6];
    for (int i = 0; i < vertices.size() / 4; i++) {
      setIndicesCCW(offset);
      offset += 4;
    }
  }

  private Collection<Vector4f> getFragmentVertices() {
    Collection<Vector4f> result = new ArrayList<>(vertices.size());
    for (Vector4f v : fragmentVertices) {
      result.add(new Vector4f(v));
    }
    return result;
  }

  @Override
  public Collection<Vertex> getVertices() {
    return vertices;
  }

  @Override
  public short[] getIndices() {
    return super.indices;
  }
}