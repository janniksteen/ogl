package dk.norgaardsteen.ogl.font;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
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
  private static int unicodePointStart = 0x0000;
  private static int unicodePointEnd = 0x00ff;

  private static int ATLAS_DIM = 16;

  private List<Integer> displayableGlyphs;

  private static final String FILE_HEAD_COMMENT = "# line layout: unicode, glyph atlas x pos, glyph atlas y pos, glyph x size, glyph y size, baseline offset";

  @Option(name = "-list", usage = "List all font names that can be used by this utility.")
  private boolean listAllFonts;

  @Option(name = "-bitmap", usage = "Creates a bitmap font atlas of the given [name,style{PLAIN|BOLD|ITALIC|BOLDITALIC},pointsize], e.g.: \"Liberation Sans\",plain,10")
  private String bitmap;

  @Option(name = "-out", usage = "Output directory of PNG file")
  private String outputDirectory;

  public void createFont(String name, int style, int size) {
    Font font = new Font(name, style, size);
    System.out.println("Using font: " + font.getName() + " " + font.getSize() + " (num glyphs: " + font.getNumGlyphs() +")");

    // filter out avaliable glyphs
    displayableGlyphs = new ArrayList<Integer>();
    int discard = 0;
    for (int unicode = unicodePointStart; unicode <= unicodePointEnd; unicode++) {
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

    BufferedImage fontImage = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
    Graphics fontGraphics = fontImage.getGraphics();
    fontGraphics.setFont(font);
    FontMetrics fontMetrics = fontGraphics.getFontMetrics(font);

    int tileHeight = fontMetrics.getHeight();
    int tileWidth = fontMetrics.getMaxAdvance();

    String fileBaseName = outputDirectory + "/uc0x" + Integer.toHexString(unicodePointStart) + "_0x" + Integer.toHexString(unicodePointEnd) + "_" + font.getName() + "_x" + tileWidth + "_y" + tileHeight;
    String atlasDescriptionFileName = fileBaseName + ".fnt";
    PrintWriter descriptionWriter = null;
    try {
      descriptionWriter = new PrintWriter(new File(atlasDescriptionFileName));
      descriptionWriter.println(FILE_HEAD_COMMENT);
      descriptionWriter.print(FontDescription.ATLAS_DIM_KEY);
      descriptionWriter.print(ATLAS_DIM * tileWidth);
      descriptionWriter.print(",");
      descriptionWriter.println(ATLAS_DIM * tileHeight);
    } catch (FileNotFoundException e) {
      System.err.println("Could not open font description file " + atlasDescriptionFileName);
      System.exit(-1);
    }

    BufferedImage atlas = new BufferedImage(ATLAS_DIM * tileWidth, ATLAS_DIM * tileHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics atlasGraphics = atlas.getGraphics();
    atlasGraphics.setFont(font);
    fontMetrics = atlasGraphics.getFontMetrics(font);

    int drawXPos = 0;
    int drawYPos = 0;

    for (int cursor = 0; cursor < glyphs.length(); cursor++) {
      char c = glyphs.charAt(cursor);
      int glyphXsize = fontMetrics.stringWidth(new String(new char[]{c}));
      int glyphYsize = tileHeight;

      atlasGraphics.drawString(Character.toString(c), drawXPos, fontMetrics.getMaxAscent() + drawYPos);

      descriptionWriter.print((int) c);
      descriptionWriter.print(",");
      descriptionWriter.print(drawXPos);
      descriptionWriter.print(",");
      descriptionWriter.print(drawYPos);
      descriptionWriter.print(",");
      descriptionWriter.print(glyphXsize);
      descriptionWriter.print(",");
      descriptionWriter.print(glyphYsize);
      descriptionWriter.print(",");
      descriptionWriter.println("0"); // y-offset: relative amount to move the glyph down to align it to a common glyph baseline

      if ((cursor + 1) % ATLAS_DIM == 0) {
        drawXPos = 0;
        drawYPos += tileHeight;
      } else {
        drawXPos += glyphXsize;
      }
    }
    atlasGraphics.dispose();
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