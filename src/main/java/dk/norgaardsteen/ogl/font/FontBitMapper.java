package dk.norgaardsteen.ogl.font;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: jns
 * Date: 12/1/13
 * Time: 9:19 PM
 */
public class FontBitMapper {

  private enum FontStyle {
    PLAIN,
    BOLD,
    ITALIC,
    BOLDITALIC
  }

  private static List<String> avaliableFontNames;

  // unicode basic latin1
  private static int unicodeStart = 0x0020;
  private static int unicodeEnd = 0x00ff;

  private List<Integer> displayableGlyphs;

  private static final int GLYPH_TILE_DIMENSION_X = 10;
  private static final int GLYPH_TILE_DIMENSION_Y = 14;
  private static final int GLYPH_PADDING_X = 2;
  private static final int GLYPH_PADDING_Y = 6;
  private static final int ATLAS_DIMENSION = 16;

  @Option(name = "-list", usage = "List all font names that can be used by this utility.")
  private boolean listAllFonts;

  @Option(name = "-bitmap", usage = "Creates a bitmap font atlas of the given [name,style{PLAIN|BOLD|ITALIC|BOLDITALIC},pointsize], e.g.: \"Liberation Sans\",plain,10")
  private String bitmap;

  @Option(name = "-out", usage = "Output directory of PNG file")
  private String outputDirectory;

  public void createFont(String name, int style, int size) {
    Font font = new Font(name, style, size);
    System.out.println("Using font: " + font.getName() + " (num glyphs: " + font.getNumGlyphs() + ")");

    // filter out avaliable glyphs
    displayableGlyphs = new ArrayList<Integer>();
    int discard = 0;
    for (int unicode = unicodeStart; unicode <= unicodeEnd; unicode++) {
      if (font.canDisplay(unicode)) {
        displayableGlyphs.add(new Integer(unicode));
      } else {
        discard++;
      }
    }

    StringBuffer glyphs = new StringBuffer();
    for (Integer unicode : displayableGlyphs) {
      glyphs.append((char)unicode.intValue());
    }

    System.out.println("Avaliable glyphs: " + glyphs.toString());
    System.out.println("Discarded " + discard + " as not avaliable in this font selection.");

    String fileBaseName = outputDirectory + "/uc0x" + Integer.toHexString(unicodeStart) + "_0x" + Integer.toHexString(unicodeEnd) + "_" + font.getName() + "_x" + GLYPH_TILE_DIMENSION_X + "_y" + GLYPH_TILE_DIMENSION_Y;
    String atlasDescriptionFileName = fileBaseName + ".fnt";
    PrintWriter descriptionWriter = null;
    try {
      descriptionWriter = new PrintWriter(new File(atlasDescriptionFileName));
      descriptionWriter.println("# unicode, glyph atlas x pos, glyph atlas y pos, glyph x size, glyph y size, y offset");
    } catch (FileNotFoundException e) {
      System.err.println("Could not open font description file " + atlasDescriptionFileName);
      System.exit(-1);
    }
    BufferedImage atlas = new BufferedImage((ATLAS_DIMENSION * GLYPH_TILE_DIMENSION_X), (ATLAS_DIMENSION * GLYPH_TILE_DIMENSION_Y), BufferedImage.TYPE_INT_ARGB);
    for (int atlasRow = 0; atlasRow < ATLAS_DIMENSION; atlasRow++) {
      for (int atlasCol = 0; atlasCol < ATLAS_DIMENSION; atlasCol++) {
        int glyphPos = (atlasRow * ATLAS_DIMENSION) + atlasCol;
        Graphics2D atlasGraphics = atlas.createGraphics();
        int atlasPosX = (atlasCol * GLYPH_TILE_DIMENSION_X) + GLYPH_PADDING_X;
        int atlasPosY = (atlasRow * GLYPH_TILE_DIMENSION_Y) + GLYPH_TILE_DIMENSION_Y / 2 + GLYPH_PADDING_Y;
        if (glyphPos < glyphs.length()) {
          char c = glyphs.charAt(glyphPos);
          atlasGraphics.drawString(Character.toString(c), atlasPosX, atlasPosY);
          descriptionWriter.print((int) c);
          descriptionWriter.print(",");
          descriptionWriter.print(atlasCol * GLYPH_TILE_DIMENSION_X);
          descriptionWriter.print(",");
          descriptionWriter.print(atlasRow * GLYPH_TILE_DIMENSION_Y);
          descriptionWriter.print(",");
          descriptionWriter.print(GLYPH_TILE_DIMENSION_X);
          descriptionWriter.print(",");
          descriptionWriter.print(GLYPH_TILE_DIMENSION_Y);
          descriptionWriter.print(",");
          descriptionWriter.println("0"); // y-offset: relative amount to move the glyph down to align in a common baseline
        } else {
          atlasGraphics.drawBytes(new byte[]{0, 0, 0, 0}, 0, 4, atlasPosX, atlasPosY);
        }
        atlasGraphics.dispose();
      }
    }

    descriptionWriter.flush();
    descriptionWriter.close();
    System.out.println("Wrote: " + atlasDescriptionFileName);

    String atlasFileName = fileBaseName + ".png";
    try {
      File file = new File(atlasFileName);
      file.createNewFile();
      ImageIO.write(atlas, "png", file);
      System.out.println("Wrote: " + atlasFileName);
    } catch (IOException e) {
      System.err.println("Failed to write output file " + atlasFileName + ". Error was: " + e.getMessage());
      System.exit(-1);
    }
  }

  public static void main(String args[]) {
    FontBitMapper fontBitMapper = new FontBitMapper();
    CmdLineParser cmdLineParser = new CmdLineParser(fontBitMapper);
    try {
      cmdLineParser.parseArgument(args);
    } catch (CmdLineException e) {
      System.out.println("Failed to parse options.");
      cmdLineParser.printUsage(System.err);
    }

    Font[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    avaliableFontNames = new ArrayList<String>(allFonts.length);
    for (Font font : allFonts) {
      avaliableFontNames.add(font.getName());
      FontMapable mapableFont = new FontMapable();
      mapableFont.name = font.getName();
      mapableFont.numberOfGlyphs = font.getNumGlyphs();
    }

    if (fontBitMapper.listAllFonts) {
      for (String fontName : avaliableFontNames) {
        System.out.println(fontName);
      }
    } else if (fontBitMapper.bitmap != null) {
      StringTokenizer tokenizer = new StringTokenizer(fontBitMapper.bitmap, ",");
      if (tokenizer.countTokens() == 3) {
        String name = null;
        int style = Font.PLAIN;
        int size = 10;
        name = tokenizer.nextToken();
        if (!avaliableFontNames.contains(name)) {
          System.err.println("Font '" + name + "' not avaliable on this platform.");
          System.exit(-1);
        }
        String bitmapStyle = tokenizer.nextToken();
        if (bitmapStyle.equalsIgnoreCase(FontStyle.PLAIN.toString())) {
          style = Font.PLAIN;
        } else if (bitmapStyle.equalsIgnoreCase(FontStyle.ITALIC.toString())) {
          style = Font.ITALIC;
        } else if (bitmapStyle.equalsIgnoreCase(FontStyle.BOLD.toString())) {
          style = Font.BOLD;
        } else if (bitmapStyle.equalsIgnoreCase(FontStyle.BOLDITALIC.toString())) {
          style = Font.BOLD | Font.ITALIC;
        } else {
          System.err.println("Unknown font style: " + bitmapStyle);
          System.exit(-1);
        }
        size = Integer.parseInt(tokenizer.nextToken());
        fontBitMapper.createFont(name, style, size);
      } else if (fontBitMapper.outputDirectory == null) {
        System.err.println("No output file specified.");
        System.exit(-1);
      } else {
        System.err.println("Insufficient bitmap value: " + fontBitMapper.bitmap);
        System.exit(-1);
      }
    } else {
      cmdLineParser.printUsage(System.err);
    }
  }
}