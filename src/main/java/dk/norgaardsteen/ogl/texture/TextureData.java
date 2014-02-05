package dk.norgaardsteen.ogl.texture;

/**
 * User: jns
 * Date: 1/29/14
 * Time: 8:22 PM
 */
public class TextureData {
  private int w = 0;
  private int h = 0;
  private byte[] data;

  public TextureData(byte[] data, int w, int h) {
    this.w = w;
    this.h = h;
    this.data = data;
  }

  public int getW() {
    return w;
  }

  public int getH() {
    return h;
  }

  public byte[] getData() {
    return data;
  }

  public String toString() {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < data.length; i++) {
      if ((i % 4 == 0) && i + 4 < data.length) {
        b.append("[").
          append(i / 4).
          append("]").
          append("rgba:").
          append("{").
          append(Byte.toString(data[i])).
          append(",").
          append(Byte.toString(data[i + 1])).
          append(",").
          append(Byte.toString(data[i + 2])).
          append(",").
          append(Byte.toString(data[i + 3])).
          append("}\n");
      }
    }
    return b.toString();
  }
}