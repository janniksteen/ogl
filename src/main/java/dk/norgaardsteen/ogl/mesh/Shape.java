package dk.norgaardsteen.ogl.mesh;

import org.lwjgl.util.vector.Matrix4f;

import java.util.Collection;

/**
 * User: jns
 * Date: 11/19/13
 * Time: 4:41 PM
 */
public interface Shape {

  public static final byte DIRT_BROWN = 0;
  public static final byte DIRT_LIGHT_DARK_BROWN_SAND = 1;
  public static final byte DIRT_LIGHT_BROW_GREY = 2;
  public static final byte DIRT_DARK_SWAMP = 3;
  public static final byte DIRT_GARDEN = 4;
  public static final byte FRESH_CUT_GRASS = 5;
  public static final byte SUMMER_GRASS = 6;
  public static final byte DRY_GRASS = 7;
  public static final byte VERY_GREEN_GRASS = 8;

  public Matrix4f getModelMatrix();
  public Collection<Vertex> getVertices();
  public short[] getIndices();
  public byte getType();
}