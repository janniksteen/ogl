package dk.norgaardsteen.ogl.mesh;

/**
 * User: jns
 * Date: 12/16/13
 * Time: 10:01 PM
 */
public abstract class QuadShape {

  public static final int INDICES_ELEMENT_COUNT = 6;
  protected short[] indices;
  protected int indicesIdx = 0;

  protected void setIndicesCCW(int vertexOffset) {
    if (indices == null) {
      throw new RuntimeException("Indices not initialized.");
    }
    indices[indicesIdx++] = (short)(0 + vertexOffset);
    indices[indicesIdx++] = (short)(1 + vertexOffset);
    indices[indicesIdx++] = (short)(2 + vertexOffset);
    indices[indicesIdx++] = (short)(2 + vertexOffset);
    indices[indicesIdx++] = (short)(3 + vertexOffset);
    indices[indicesIdx++] = (short)(0 + vertexOffset);
  }

  protected void setIndicesCW(int vertexOffset) {
    if (indices == null) {
      throw new RuntimeException("Indices not initialized.");
    }
    indices[indicesIdx++] = (short)(0 + vertexOffset);
    indices[indicesIdx++] = (short)(2 + vertexOffset);
    indices[indicesIdx++] = (short)(1 + vertexOffset);
    indices[indicesIdx++] = (short)(0 + vertexOffset);
    indices[indicesIdx++] = (short)(3 + vertexOffset);
    indices[indicesIdx++] = (short)(2 + vertexOffset);
  }

  protected void dumpIndices() {
    for (int i = 0; i < indices.length; i = i + 6) {
      if (i % 6 == 0) {
        System.out.println(i + " ---------------------------------");
      }
      System.out.println("indices[" + indices[i] + "," + indices[i+1] + "," + indices[i+2] + "," + indices[i+3] + "," + indices[i+4] + "," + indices[i+5] + "]");
    }
  }

}
