package dk.norgaardsteen.ogl.mesh;

import dk.norgaardsteen.ogl.math.Vectors;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: jns
 * Date: 1/29/14
 * Time: 9:50 AM
 */
public class SimpleVoxel extends QuadShape {

  // matrices
  private static final Matrix4f FACE_LEFT;
  private static final Matrix4f FACE_RIGHT;
  private static final Matrix4f FACE_FRONT;
  private static final Matrix4f FACE_BACK;
  private static final Matrix4f FACE_TOP;
  private static final Matrix4f FACE_BOTTOM;

  // texture coordinates
  private static final float[][] TEXTURE_COORDINATES = {
    {0.0f, 1.0f},
    {1.0f, 1.0f},
    {1.0f, 0.0f},
    {0.0f, 0.0f}
  };

  static {
    FACE_FRONT = new Matrix4f();
    Matrix4f.rotate((float)Math.toRadians(0), Vectors.X_AXIS, FACE_FRONT, FACE_FRONT);
    Matrix4f.rotate((float)Math.toRadians(0), Vectors.Y_AXIS, FACE_FRONT, FACE_FRONT);
    Matrix4f.rotate((float)Math.toRadians(0), Vectors.X_AXIS, FACE_FRONT, FACE_FRONT);
  }

  static {
    FACE_BACK = new Matrix4f();
    Matrix4f.rotate((float) Math.toRadians(0), Vectors.Z_AXIS, FACE_BACK, FACE_BACK);
    Matrix4f.rotate((float)Math.toRadians(-180), Vectors.Y_AXIS, FACE_BACK, FACE_BACK);
    Matrix4f.rotate((float) Math.toRadians(0), Vectors.X_AXIS, FACE_BACK, FACE_BACK);
  }

  static {
    FACE_LEFT = new Matrix4f();
    Matrix4f.rotate((float)Math.toRadians(0), Vectors.Z_AXIS, FACE_LEFT, FACE_LEFT);
    Matrix4f.rotate((float)Math.toRadians(-90), Vectors.Y_AXIS, FACE_LEFT, FACE_LEFT);
    Matrix4f.rotate((float) Math.toRadians(0), Vectors.X_AXIS, FACE_LEFT, FACE_LEFT);
  }

  static {
    FACE_RIGHT = new Matrix4f();
    Matrix4f.rotate((float)Math.toRadians(0), Vectors.Z_AXIS, FACE_RIGHT, FACE_RIGHT);
    Matrix4f.rotate((float)Math.toRadians(-270), Vectors.Y_AXIS, FACE_RIGHT, FACE_RIGHT);
    Matrix4f.rotate((float) Math.toRadians(0), Vectors.X_AXIS, FACE_RIGHT, FACE_RIGHT);
  }

  static {
    FACE_TOP = new Matrix4f();
    Matrix4f.rotate((float)Math.toRadians(0), Vectors.Z_AXIS, FACE_TOP, FACE_TOP);
    Matrix4f.rotate((float)Math.toRadians(0), Vectors.Y_AXIS, FACE_TOP, FACE_TOP);
    Matrix4f.rotate((float)Math.toRadians(270), Vectors.X_AXIS, FACE_TOP, FACE_TOP);
  }

  static {
    FACE_BOTTOM = new Matrix4f();
    Matrix4f.rotate((float)Math.toRadians(0), Vectors.Z_AXIS, FACE_BOTTOM, FACE_BOTTOM);
    Matrix4f.rotate((float)Math.toRadians(0), Vectors.Y_AXIS, FACE_BOTTOM, FACE_BOTTOM);
    Matrix4f.rotate((float)Math.toRadians(-270), Vectors.X_AXIS, FACE_BOTTOM, FACE_BOTTOM);
  }

  private static final Matrix4f[] FACES = {
    FACE_FRONT,
    FACE_BACK,
    FACE_LEFT,
    FACE_RIGHT,
    FACE_TOP,
    FACE_BOTTOM
  };

  public SimpleVoxel() {
    vertices = new ArrayList<>();
    indices = new short[FACES.length * 6];

    for (int face = 0; face < FACES.length; face++) {
      vertices.addAll(toVertexTypes(transformFaceVertices(Vectors.createFace(), FACES[face])));
      setIndicesCCW(vertices.size() - 4);
    }
  }

  private Collection<Vertex> toVertexTypes(Collection<Vector4f> vertices) {
    Collection<Vertex> result = new ArrayList<>(vertices.size());
    int idx = 0;
    for (Vector4f v4 : vertices) {
      result.add(new Vertex().setXYZW(v4.x, v4.y, v4.z, v4.w).setST(TEXTURE_COORDINATES[idx][0], TEXTURE_COORDINATES[idx][1]));
      idx++;
    }
    return result;
  }

  private Collection<Vector4f> transformFaceVertices(Collection<Vector3f> faceVertices, Matrix4f face) {
    Collection<Vector4f> result = new ArrayList<>(faceVertices.size());
    for (Vector3f v3 : faceVertices) {
      Vector4f v4 = new Vector4f(v3.x, v3.y, v3.z, 1.0f);
      Matrix4f.transform(face, v4, v4);
      result.add(v4);
    }
    return result;
  }

  @Override
  public Matrix4f getModelMatrix() {
    return null;
  }

  @Override
  public short[] getIndices() {
    return indices;
  }

  @Override
  public byte getType() {
    return 0;
  }
}