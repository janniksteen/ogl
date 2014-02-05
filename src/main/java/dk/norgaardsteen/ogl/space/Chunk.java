package dk.norgaardsteen.ogl.space;

/**
 * User: jns
 * Date: 1/25/14
 * Time: 7:42 PM
 */
public class Chunk {

  private float[][][][][] chunkElements = new float[32][32][32][1][];

  public Chunk() {};

  public void addElement(int x, int y, int z, int type, float[] vertices) {
    chunkElements[x][y][z][type] = vertices;
  }
}
