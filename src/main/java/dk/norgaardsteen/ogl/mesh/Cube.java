package dk.norgaardsteen.ogl.mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jns
 * Date: 11/19/13
 * Time: 4:38 PM
 */
public class Cube implements Shape {

  private static final float[] RED = {0.545f, 0.000f, 0.000f};
  private static final float[] GREEN = {0.000f, 0.392f, 0.000f};
  private static final float[] BLUE = {0.000f, 0.000f, 0.545f};
  private static final float[] DARK_BLUE = {0.282f, 0.239f, 0.545f};
  private static final float[] ORANGE = {1.000f, 0.549f, 0.000f};
  private static final float[] DIM_GREY = {0.412f, 0.412f, 0.412f};

  private List<Vertex> vertices;
  private byte[] indices;
  private int indicesIdx = 0;

  public Cube() {
    vertices = new ArrayList<>();
    indices = new byte[6 *6];
    byte vertexOffset = 0;
    // front (from camera perspective)
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, -0.5f).setRGB(RED).setST(0,0)); // top left
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, -0.5f).setRGB(RED).setST(0,1)); // bottom left
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, -0.5f).setRGB(RED).setST(1,1)); // bottom right
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, -0.5f).setRGB(RED).setST(1,0)); // top right
    setIndices(vertexOffset);
    // rear
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, 0.5f).setRGB(GREEN));
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, 0.5f).setRGB(GREEN));
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, 0.5f).setRGB(GREEN));
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, 0.5f).setRGB(GREEN));
    setIndices(vertexOffset + 4);
    // left
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, 0.5f).setRGB(BLUE));
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, -0.5f).setRGB(BLUE));
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, -0.5f).setRGB(BLUE));
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, 0.5f).setRGB(BLUE));
    setIndices(vertexOffset + 8);
    // right
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, 0.5f).setRGB(DARK_BLUE));
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, -0.5f).setRGB(DARK_BLUE));
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, -0.5f).setRGB(DARK_BLUE));
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, 0.5f).setRGB(DARK_BLUE));
    setIndices(vertexOffset + 12);
    // top
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, 0.5f).setRGB(ORANGE));
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, 0.5f).setRGB(ORANGE));
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, -0.5f).setRGB(ORANGE));
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, -0.5f).setRGB(ORANGE));
    setIndices(vertexOffset + 16);
    // bottom
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, 0.5f).setRGB(DIM_GREY));
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, 0.5f).setRGB(DIM_GREY));
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, -0.5f).setRGB(DIM_GREY));
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, -0.5f).setRGB(DIM_GREY));
    setIndices(vertexOffset + 20);
  }

  @Override
  public List<Vertex> getVertices() {
    return vertices;
  }

  @Override
  public byte[] getIndices() {
    return indices;
  }

  private void setIndices(int vertexOffset) {
    indices[indicesIdx++] = (byte)(0 + vertexOffset);
    indices[indicesIdx++] = (byte)(1 + vertexOffset);
    indices[indicesIdx++] = (byte)(2 + vertexOffset);
    indices[indicesIdx++] = (byte)(2 + vertexOffset);
    indices[indicesIdx++] = (byte)(3 + vertexOffset);
    indices[indicesIdx++] = (byte)(0 + vertexOffset);
  }
}