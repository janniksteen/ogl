package dk.norgaardsteen.ogl.util;

/**
 * User: jns
 * Date: 12/6/13
 * Time: 9:08 PM
 */
public class Dump {
  public static void floatArray(String name, float[] ofValues) {
    StringBuffer buffer = new StringBuffer();
    if (name != null) {
      buffer.append(name);
      buffer.append(":");
    }
    buffer.append("{");
    for (int i = 0; i < ofValues.length; i++) {
      buffer.append(ofValues[i]);
      if (i+1 < ofValues.length) {
        buffer.append(',');
      }
    }
    buffer.append("}");
    System.out.println(buffer.toString());
  }

  public static void byteArray(String name, byte[] ofValues) {
    StringBuffer buffer = new StringBuffer();
    if (name != null) {
      buffer.append(name);
      buffer.append(":");
    }
    buffer.append("{");
    for (int i = 0; i < ofValues.length; i++) {
      buffer.append(ofValues[i]);
      if (i+1 < ofValues.length) {
        buffer.append(',');
      }
    }
    buffer.append("}");
    System.out.println(buffer.toString());
  }
}
