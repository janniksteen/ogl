package dk.norgaardsteen.ogl.mesh;

import java.util.Arrays;

/**
 * User: jns
 * Date: 12/4/13
 * Time: 4:08 PM
 */
public class Vertex {
  private float[] xyzw = new float[] {0f, 0f, 0f, 1f};
  private float[] rgba = new float[] {1f, 1f, 1f, 1f};
  private float[] st = new float[] {0f, 0f};

  // The amount of elements a position has
  public static final int POSITION_ELEMENT_COUNT = 4;
  // The amount of elements a color has
  public static final int COLOR_ELEMENT_COUNT = 4;
  // The amount of element a texture coordinates has
  public static final int TEXTURE_COORDINATE_ELEMENT_COUNT = 2;
  // The total amount of elements that a vertex has
  public static final int TOTAL_ELEMENT_COUNT = POSITION_ELEMENT_COUNT + COLOR_ELEMENT_COUNT + TEXTURE_COORDINATE_ELEMENT_COUNT;
  // The amount of bytes an element has
  public static final int ELEMENT_BYTES = 4;
  // The total size of a vertex in bytes
  public static final int TOTAL_SIZE_IN_BYTES = ELEMENT_BYTES * TOTAL_ELEMENT_COUNT;

  public Vertex setXY(float x, float y) {
    this.setXYZW(x, y, 0f, 1f);
    return this;
  }

  public Vertex setXYZ(float x, float y, float z) {
    this.setXYZW(x, y, z, 1f);
    return this;
  }

  public Vertex setRGB(float r, float g, float b) {
    this.setRGBA(r, g, b, 1f);
    return this;
  }

  public Vertex setRGB(float[] rgb) {
    this.setRGB(rgb[0], rgb[1], rgb[2]);
    return this;
  }

  public Vertex setXYZW(float x, float y, float z, float w) {
    this.xyzw = new float[] {x, y, z, w};
    return this;
  }

  public Vertex setRGBA(float r, float g, float b, float a) {
    this.rgba = new float[] {r, g, b, 1f};
    return this;
  }

  public Vertex setST(float s, float t) {
    this.st = new float[]{s, t};
    return this;
  }

  public float[] getXY() {
    return new float[] {this.xyzw[0], this.xyzw[1]};
  }

  public float[] getXYZW() {
    return new float[] {this.xyzw[0], this.xyzw[1], this.xyzw[2], this.xyzw[3]};
  }

  public float[] getRGBA() {
    return new float[] {this.rgba[0], this.rgba[1], this.rgba[2], this.rgba[3]};
  }

  public float[] getST() {
    return new float[] {this.st[0], this.st[1]};

  }

  @Override
  public String toString() {
    StringBuffer b = new StringBuffer();
    b.append("[x:");
    b.append(this.xyzw[0]);
    b.append(",y:");
    b.append(this.xyzw[1]);
    b.append(",z:");
    b.append(this.xyzw[2]);
    b.append(",w:");
    b.append(this.xyzw[3]);
    b.append("|");
    b.append("s:");
    b.append(this.st[0]);
    b.append(",t:");
    b.append(this.st[1]);
    b.append("|");
    b.append("r:");
    b.append(this.rgba[0]);
    b.append(",g:");
    b.append(this.rgba[1]);
    b.append(",b:");
    b.append(this.rgba[2]);
    b.append(",a:");
    b.append(this.rgba[3]);
    b.append("]");
    return b.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Vertex vertex = (Vertex) o;

    if (!Arrays.equals(rgba, vertex.rgba)) return false;
    if (!Arrays.equals(st, vertex.st)) return false;
    if (!Arrays.equals(xyzw, vertex.xyzw)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(xyzw);
    result = 31 * result + Arrays.hashCode(rgba);
    result = 31 * result + Arrays.hashCode(st);
    return result;
  }
}