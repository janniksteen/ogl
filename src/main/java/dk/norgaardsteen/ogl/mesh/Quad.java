package dk.norgaardsteen.ogl.mesh;

/**
 * User: jns
 * Date: 11/19/13
 * Time: 4:38 PM
 */
public class Quad implements DeprecatedShape {

  private static final int NUM_OF_FACES = 6;
  private static final int NUM_OF_FACE_VERTICES = 6;
  private static final float[] RED = {0.545f, 0.000f, 0.000f};
  private static final float[] GREEN = {0.000f, 0.392f, 0.000f};
  private static final float[] BLUE = {0.000f, 0.000f, 0.545f};
  private static final float[] DARK_BLUE = {0.282f, 0.239f, 0.545f};
  private static final float[] ORANGE = {1.000f, 0.549f, 0.000f};
  private static final float[] DIM_GREY = {0.412f, 0.412f, 0.412f};

  private static final float[][] FACE_COLORS = {RED, GREEN, BLUE, ORANGE, DIM_GREY, DARK_BLUE};

  public Quad() {
    colors = new float[NUM_OF_FACES * NUM_OF_FACE_VERTICES * 3]; // number of faces * number of vertices of each face * number of values of each color
    for (int face = 0; face < NUM_OF_FACES; face++) {
      for (int j = 0; j < NUM_OF_FACE_VERTICES; j++) {
        colors[(face*18)] = FACE_COLORS[face][0];
        colors[(face*18)+1] = FACE_COLORS[face][1];
        colors[(face*18)+2] = FACE_COLORS[face][2];

        colors[(face*18)+3] = FACE_COLORS[face][0];
        colors[(face*18)+4] = FACE_COLORS[face][1];
        colors[(face*18)+5] = FACE_COLORS[face][2];

        colors[(face*18)+6] = FACE_COLORS[face][0];
        colors[(face*18)+7] = FACE_COLORS[face][1];
        colors[(face*18)+8] = FACE_COLORS[face][2];

        colors[(face*18)+9] = FACE_COLORS[face][0];
        colors[(face*18)+10] = FACE_COLORS[face][1];
        colors[(face*18)+11] = FACE_COLORS[face][2];

        colors[(face*18)+12] = FACE_COLORS[face][0];
        colors[(face*18)+13] = FACE_COLORS[face][1];
        colors[(face*18)+14] = FACE_COLORS[face][2];

        colors[(face*18)+15] = FACE_COLORS[face][0];
        colors[(face*18)+16] = FACE_COLORS[face][1];
        colors[(face*18)+17] = FACE_COLORS[face][2];
      }
    }
  }

  private static final float vertices[] = {
    // face 1
    -1.0f, -1.0f, -1.0f, 1.0f,
    1.0f, -1.0f, -1.0f, 1.0f,
    -1.0f, 1.0f, -1.0f, 1.0f,

    1.0f, -1.0f, -1.0f, 1.0f,
    1.0f, 1.0f, -1.0f, 1.0f,
    -1.0f, 1.0f, -1.0f, 1.0f,

    // face 2
    -1.0f, -1.0f, 1.0f, 1.0f,
    1.0f, -1.0f, 1.0f, 1.0f,
    -1.0f, 1.0f, 1.0f, 1.0f,

    1.0f, -1.0f, 1.0f, 1.0f,
    1.0f, 1.0f, 1.0f, 1.0f,
    -1.0f, 1.0f, 1.0f, 1.0f,

    // face 3
    -1.0f, 1.0f, 1.0f, 1.0f,
    -1.0f, -1.0f, 1.0f, 1.0f,
    -1.0f, 1.0f, -1.0f, 1.0f,

    -1.0f, -1.0f, -1.0f, 1.0f,
    -1.0f, 1.0f, -1.0f, 1.0f,
    -1.0f, -1.0f, 1.0f, 1.0f,

    // face 4
    1.0f, 1.0f, 1.0f, 1.0f,
    1.0f, -1.0f, 1.0f, 1.0f,
    1.0f, 1.0f, -1.0f, 1.0f,

    1.0f, -1.0f, -1.0f, 1.0f,
    1.0f, 1.0f, -1.0f, 1.0f,
    1.0f, -1.0f, 1.0f, 1.0f,

    // face 5
    -1.0f, 1.0f, 1.0f, 1.0f,
    -1.0f, 1.0f, -1.0f, 1.0f,
    1.0f, 1.0f, -1.0f, 1.0f,

    1.0f, 1.0f, -1.0f, 1.0f,
    1.0f, 1.0f, 1.0f, 1.0f,
    -1.0f, 1.0f, 1.0f, 1.0f,

    // face 6
    -1.0f, -1.0f, 1.0f, 1.0f,
    -1.0f, -1.0f, -1.0f, 1.0f,
    1.0f, -1.0f, -1.0f, 1.0f,

    1.0f, -1.0f, -1.0f, 1.0f,
    1.0f, -1.0f, 1.0f, 1.0f,
    -1.0f, -1.0f, 1.0f, 1.0f
  };

  private static float[] colors = new float[]{};

  @Override
  public float[] vertices() {
    return vertices;
  }

  @Override
  public int vertexPositionElementCount() {
    return 4;
  }

  @Override
  public float[] colors() {
    return colors;
  }

  public String toString() {
    StringBuffer out = new StringBuffer();
    out.append("Vertices:\n");
    for (int i = 0; i < vertices.length; i = i + 4) {
      out.append(vertices[i] + ", ");
      out.append(vertices[i+1] + ", ");
      out.append(vertices[i+2] + ", ");
      out.append(vertices[i+3] + "\n");
    }
    out.append("\nColors:\n");
    for (int i = 0; i < colors.length; i = i + 3) {
      out.append(colors[i] + ", ");
      out.append(colors[i + 1] + ", ");
      out.append(colors[i + 2] + "\n");
    }
    return out.toString();
  }
}