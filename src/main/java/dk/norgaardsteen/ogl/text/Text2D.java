package dk.norgaardsteen.ogl.text;

import dk.norgaardsteen.ogl.font.FontDescription;
import dk.norgaardsteen.ogl.math.Positions;
import dk.norgaardsteen.ogl.resource.ApplicationContext;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jns
 * Date: 12/11/13
 * Time: 2:15 PM
 */
public class Text2D {

  public static List<TexturedTextTile> createTextTiles(String text, FontDescription fontDescription, int xPad, int yPad, int displayX, int displayY, int existingTextSize) {
    List<TexturedTextTile> texturedTextTiles = new ArrayList<>();
    int vertexOffset = existingTextSize * 4;
    int tileX = 0;
    int tileY = displayY - yPad;

    for (int cursor = 0; cursor < text.length(); cursor++) {
      int unicode = text.codePointAt(cursor);
      TexturedTextTile texturedTextTile = new TexturedTextTile((char)unicode);
      FontDescription.GlyphMeta glyphMeta = fontDescription.getGlyphMeta().get(new Integer(unicode));

      // create a character tile
      float leftPos = tileX;
      float rightPos = tileX + glyphMeta.glyphXSize; // x right
      float bottomPos = tileY - glyphMeta.glyphYSize; // y bottom
      float topPos = tileY; // y top

      // resolve the position of the character texture in the font atlas (texture)

      float atlasXScale = 1.0f / fontDescription.atlasXDimension;
      float atlasYScale = 1.0f / fontDescription.atlasYDimension;

      float s_0 = glyphMeta.atlasXPos * atlasXScale; // S=0
      float s_1 = (glyphMeta.atlasXPos + glyphMeta.glyphXSize) * atlasXScale; // S=1
      float t_0 = glyphMeta.atlasYPos * atlasYScale; // T=0
      float t_1 = (glyphMeta.atlasYPos + glyphMeta.glyphYSize) * atlasYScale; // T=1

      texturedTextTile.add(Positions.translateToClipSpace(leftPos, topPos, displayX, displayY), new Vector2f(s_0, t_0)); // left top
      texturedTextTile.add(Positions.translateToClipSpace(leftPos, bottomPos, displayX, displayY), new Vector2f(s_0, t_1)); // left bottom
      texturedTextTile.add(Positions.translateToClipSpace(rightPos, bottomPos, displayX, displayY), new Vector2f(s_1, t_1)); // right bottom
      texturedTextTile.add(Positions.translateToClipSpace(rightPos, topPos, displayX, displayY), new Vector2f(s_1, t_0)); // right top

      texturedTextTile.setIndices(vertexOffset);
      texturedTextTiles.add(texturedTextTile);

      tileX += glyphMeta.glyphXSize + xPad;
      vertexOffset += 4;

    }
    return texturedTextTiles;
  }
}