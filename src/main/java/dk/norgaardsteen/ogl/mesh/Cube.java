package dk.norgaardsteen.ogl.mesh;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: jns
 * Date: 11/19/13
 * Time: 4:38 PM
 */
public class Cube extends QuadShape implements Shape {

  private static final float[] RED = {0.545f, 0.000f, 0.000f};
  private static final float[] GREEN = {0.000f, 0.392f, 0.000f};
  private static final float[] BLUE = {0.000f, 0.000f, 0.545f};
  private static final float[] DARK_BROWN = {0.36f, 0.25f, 0.20f};
  private static final float[] ORANGE = {1.000f, 0.549f, 0.000f};
  private static final float[] DIM_GREY = {0.412f, 0.412f, 0.412f};

  private Collection<Vertex> vertices;

  public Cube() {
    vertices = new ArrayList<>();
    indices = new short[6 *6];
    byte vertexOffset = 0;
    // rear
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, -0.5f).setRGB(RED).setST(0,0)); // top left (0)
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, -0.5f).setRGB(RED).setST(0,1)); // bottom left (1)
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, -0.5f).setRGB(RED).setST(1,1)); // bottom right (2)
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, -0.5f).setRGB(RED).setST(1,0)); // top right (3)
    setIndicesCW(vertexOffset);
    // front
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, 0.5f).setRGB(GREEN).setST(0,0)); // (0)
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, 0.5f).setRGB(GREEN).setST(0,1)); // (1)
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, 0.5f).setRGB(GREEN).setST(1,1)); // (2)
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, 0.5f).setRGB(GREEN).setST(1,0)); // (3)
    setIndicesCCW(vertexOffset + 4);
    // left
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, 0.5f).setRGB(BLUE).setST(0,0));
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, -0.5f).setRGB(BLUE).setST(0,1));
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, -0.5f).setRGB(BLUE).setST(1,1));
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, 0.5f).setRGB(BLUE).setST(1,0));
    setIndicesCCW(vertexOffset + 8);
    // right
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, 0.5f).setRGB(DARK_BROWN).setST(0,0));
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, -0.5f).setRGB(DARK_BROWN).setST(0,1));
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, -0.5f).setRGB(DARK_BROWN).setST(1,1));
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, 0.5f).setRGB(DARK_BROWN).setST(1,0));
    setIndicesCW(vertexOffset + 12);
    // top
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, 0.5f).setRGB(ORANGE).setST(0,0));
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, 0.5f).setRGB(ORANGE).setST(0,1));
    vertices.add(new Vertex().setXYZ(0.5f, 0.5f, -0.5f).setRGB(ORANGE).setST(1,1));
    vertices.add(new Vertex().setXYZ(-0.5f, 0.5f, -0.5f).setRGB(ORANGE).setST(1,0));
    setIndicesCCW(vertexOffset + 16);
    // bottom
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, 0.5f).setRGB(DIM_GREY).setST(0,0));
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, 0.5f).setRGB(DIM_GREY).setST(0,1));
    vertices.add(new Vertex().setXYZ(0.5f, -0.5f, -0.5f).setRGB(DIM_GREY).setST(1,1));
    vertices.add(new Vertex().setXYZ(-0.5f, -0.5f, -0.5f).setRGB(DIM_GREY).setST(1,0));
    setIndicesCW(vertexOffset + 20);
  }

  @Override
  public Collection<Vertex> getVertices() {
    return vertices;
  }

  @Override
  public short[] getIndices() {
    return indices;
  }
}