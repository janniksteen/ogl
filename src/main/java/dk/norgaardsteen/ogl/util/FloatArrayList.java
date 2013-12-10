package dk.norgaardsteen.ogl.util;

/**
 * User: jns
 * Date: 12/6/13
 * Time: 7:15 PM
 */
public class FloatArrayList {

  private static float[] array = new float[]{};

  public void put(float[] inArray) {
    float[] newArray = new float[this.array.length + inArray.length];
    System.arraycopy(inArray, 0, newArray, this.array.length, inArray.length);
    this.array = newArray;
  }

  public float[] array() {
    return array;
  }
}
