package dk.norgaardsteen.ogl.mesh.color;

/**
 * User: jns
 * Date: 2/5/14
 * Time: 11:23 AM
 */
public class ColorDescription {
  private byte r;
  private byte g;
  private byte b;
  private byte a;
  private float w;

  public ColorDescription(byte r, byte g, byte b, byte a, float w) {
    if (w < 0.0f || w > 1.0f) {
      throw new IllegalArgumentException("weight must be a number [0, 1.0]");
    }
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
    this.w = w;
  }

  public byte getR() {
    return r;
  }

  public byte getG() {
    return g;
  }

  public byte getB() {
    return b;
  }

  public byte getA() {
    return a;
  }

  public float getW() {
    return w;
  }

  public String toString() {
    return "rgbaw:[" + r + "," + g + "," + b + "," + a + "," + w + "]";
  }
}
