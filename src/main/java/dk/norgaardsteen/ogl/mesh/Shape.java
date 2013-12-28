package dk.norgaardsteen.ogl.mesh;

import java.util.Collection;

/**
 * User: jns
 * Date: 11/19/13
 * Time: 4:41 PM
 */
public interface Shape {
  public Collection<Vertex> getVertices();
  public short[] getIndices();
}