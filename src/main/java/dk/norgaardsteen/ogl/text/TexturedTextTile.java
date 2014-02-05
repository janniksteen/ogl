package dk.norgaardsteen.ogl.text;

import dk.norgaardsteen.ogl.mesh.QuadShape;
import dk.norgaardsteen.ogl.mesh.Shape;
import dk.norgaardsteen.ogl.mesh.Vertex;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: jns
 * Date: 12/11/13
 * Time: 2:17 PM
 */
public class TexturedTextTile extends QuadShape implements Shape {

  private char c;
  // The amount of bytes an element has
  public static final int ELEMENT_BYTES = 4;
  // The amount of elements a vertex has
  public static final int VERTEX_ELEMENT_COUNT = 2;
  // The amount of element a texture position has
  public static final int ST_ELEMENT_COUNT = 2;
  // The total amount of vertices that a texture tile has
  public static final int TOTAL_VERTICES_COUNT = 4;
  // The total amount of elements that a texture tile has
  public static final int TOTAL_ELEMENT_COUNT = VERTEX_ELEMENT_COUNT + ST_ELEMENT_COUNT;
  // The total size of a textured tile in bytes
  public static final int TOTAL_SIZE_IN_BYTES = TOTAL_ELEMENT_COUNT * ELEMENT_BYTES;

  private Collection<Vertex> vertices;

  public TexturedTextTile(char c) {
    indices = new short[INDICES_ELEMENT_COUNT];
    this.c = c;
    vertices = new ArrayList<>();
  }

  public void add(Vector2f vertex, Vector2f st) {
    vertices.add(new Vertex().setXY(vertex.x, vertex.y).setST(st.x, st.y));
  }

  public void setIndicesCCW(int vertexOffset) {
    super.setIndicesCCW(vertexOffset);
  }

  @Override
  public Matrix4f getModelMatrix() {
    return null;
  }

  @Override
  public Collection<Vertex> getVertices() {
    return vertices;
  }

  @Override
  public short[] getIndices() {
    return indices;
  }

  @Override
  public byte getType() {
    return 0;
  }

  public char getChar() {
    return c;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Char:");
    buffer.append(c);
    buffer.append("\n");
    buffer.append("Vertex{");
    for (Vertex vertex : vertices) {
      buffer.append("[").append(vertex.getXY()[0]).append(",").append(vertex.getXY()[1]).append("]");
    }
    buffer.append("}\n");
    buffer.append("ST{");
    for (Vertex vertex : vertices) {
      buffer.append("[").append(vertex.getST()[0]).append(",").append(vertex.getST()[1]).append("]");
    }
    buffer.append("}\n");
    buffer.append("Indices{");
    for (int i = 0; i < indices.length; i++) {
      buffer.append(indices[i]);
      if (i + 1 < indices.length) {
        buffer.append(",");
      }
    }
    buffer.append("}\n");
    return buffer.toString();
  }
}