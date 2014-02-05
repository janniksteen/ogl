package dk.norgaardsteen.ogl.space;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: jns
 * Date: 1/25/14
 * Time: 1:52 PM
 */
public class FlatWorld extends World {

  // world chunk size
  private int length = 8;
  private int depth = 8;
  private int width = 8;

  private Collection<Chunk> chunks = new ArrayList<>(length * depth * width);

  public FlatWorld() {
  }
}
