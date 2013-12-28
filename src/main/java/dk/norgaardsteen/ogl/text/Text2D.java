package dk.norgaardsteen.ogl.text;

import dk.norgaardsteen.ogl.font.FontDescription;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jns
 * Date: 12/11/13
 * Time: 2:15 PM
 */
public class Text2D {

  public static List<TexturedTextTile> createTextTiles(String text, FontDescription fontDescription) {
    return createTextTiles(text, fontDescription, 0, 0);
  }

  public static List<TexturedTextTile> createTextTiles(String text, FontDescription fontDescription, int xPad, int yPad) {
    List<TexturedTextTile> texturedTextTiles = new ArrayList<>();
    int lineRow = 0;
    int vertexOffset = 0;
    int tileX = 0;
    int tileY = 0;

    for (int charNum = 0; charNum < text.length(); charNum++) {
      System.out.println("Start create text tile");
      int unicode = text.codePointAt(charNum);
      TexturedTextTile texturedTextTile = new TexturedTextTile((char)unicode);
      System.out.println("char:" + (char)unicode);
      FontDescription.GlyphMeta glyphMeta = fontDescription.getGlyphMeta().get(new Integer(unicode));

      // create a character tile
      float leftPos = tileX;
      float rightPos = tileX + glyphMeta.glyphXSize; // x right
      float bottomPos = tileY + 0; // y bottom
      float topPos = tileY + glyphMeta.glyphYSize; // y top

      // resolve the position of the character texture in the font atlas (texture)

      float atlasXScale = 1.0f / fontDescription.atlasXDimension;
      float atlasYScale = 1.0f / fontDescription.atlasYDimension;

      float s_0 = glyphMeta.atlasXPos * atlasXScale; // S=0
      float s_1 = (glyphMeta.atlasXPos + glyphMeta.glyphXSize) * atlasXScale; // S=1
      float t_0 = glyphMeta.atlasYPos * atlasYScale; // T=0
      float t_1 = (glyphMeta.atlasYPos + glyphMeta.glyphYSize) * atlasYScale; // T=1

      texturedTextTile.add(new Vector2f(leftPos, topPos), new Vector2f(s_0, t_0)); // left top
      texturedTextTile.add(new Vector2f(leftPos, bottomPos), new Vector2f(s_0, t_1)); // left bottom
      texturedTextTile.add(new Vector2f(rightPos, bottomPos), new Vector2f(s_1, t_1)); // right bottom
      texturedTextTile.add(new Vector2f(rightPos, topPos), new Vector2f(s_1, t_0)); // right top

      texturedTextTile.setIndices(vertexOffset);

      System.out.println("Positions:[x_0=" + leftPos + ",y_0=" + bottomPos + ",x_1=" + rightPos + ",y_1=" + topPos + "]");
      System.out.println("ST:[s_0=" + s_0 + "," + "s_1=" + s_1 + ",t_0=" + t_0 + ",t_1=" + t_1 + "]");

      texturedTextTiles.add(texturedTextTile);
      System.out.println("End create text tile\n");

      tileX += glyphMeta.glyphXSize + xPad;
      vertexOffset += 4;

    }
    return texturedTextTiles;
  }
}