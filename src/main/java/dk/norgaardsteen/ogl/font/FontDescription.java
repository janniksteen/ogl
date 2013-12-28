package dk.norgaardsteen.ogl.font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * User: jns
 * Date: 12/11/13
 * Time: 2:38 PM
 */
public class FontDescription {

  public static final String ATLAS_DIM_KEY = "dim:";

  private Map<Integer, GlyphMeta> glyphMetas;
  private String descriptionFilePath;
  public short atlasXDimension;
  public short atlasYDimension;

  public class GlyphMeta {
    // unicode, glyph atlas x pos, glyph atlas y pos, glyph x size, glyph y size, y offset
    public short unicode;
    public short atlasXPos;
    public short atlasYPos;
    public short glyphXSize;
    public short glyphYSize;
    public short baselineOffset;
  }

  public FontDescription(String descriptionFilePath) {
    this.descriptionFilePath = descriptionFilePath;
  }

  public Map<Integer, GlyphMeta> getGlyphMeta() {
    if (glyphMetas == null) {
      File file = new File(descriptionFilePath);
      if (!file.exists()) {
        throw new RuntimeException("Could not load font description file " + descriptionFilePath);
      }
      try {
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
        String line = lineNumberReader.readLine();
        while (line != null) {
          if (line.startsWith(ATLAS_DIM_KEY)) {
            String dim = line.substring(ATLAS_DIM_KEY.length(), line.length());
            String[] splitLine = dim.split(",");
            this.atlasXDimension = Short.valueOf(splitLine[0]);
            this.atlasYDimension = Short.valueOf(splitLine[1]);
          } else if (!line.startsWith("#")) {
            StringTokenizer lineTokenizer = new StringTokenizer(line, ",");
            GlyphMeta glyphMeta = new GlyphMeta();
            while (lineTokenizer.hasMoreTokens()) {
              glyphMeta.unicode = Short.valueOf(lineTokenizer.nextToken());
              glyphMeta.atlasXPos = Short.valueOf(lineTokenizer.nextToken());
              glyphMeta.atlasYPos = Short.valueOf(lineTokenizer.nextToken());
              glyphMeta.glyphXSize = Short.valueOf(lineTokenizer.nextToken());
              glyphMeta.glyphYSize = Short.valueOf(lineTokenizer.nextToken());
              glyphMeta.baselineOffset = Short.valueOf(lineTokenizer.nextToken());
            }
            if (glyphMetas == null) {
              glyphMetas = new HashMap<Integer, GlyphMeta>();
            }
            glyphMetas.put(new Integer(glyphMeta.unicode), glyphMeta);
          }
          line = lineNumberReader.readLine();
        }
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Could not load font description file " + descriptionFilePath);
      } catch (IOException e) {
        throw new RuntimeException("Failed to read a line from font description " + descriptionFilePath);
      }
    }
    return glyphMetas;
  }
}