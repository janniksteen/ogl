package dk.norgaardsteen.ogl.mesh;

import java.util.List;

/**
 * User: jns
 * Date: 11/19/13
 * Time: 4:41 PM
 */
public interface Shape {
  public List<Vertex> getVertices();
  public byte[] getIndices();
}