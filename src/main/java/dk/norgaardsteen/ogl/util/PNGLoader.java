package dk.norgaardsteen.ogl.util;

import net.sourceforge.fastpng.PNGDecoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * User: jns
 * Date: 12/6/13
 * Time: 10:33 PM
 */
public class PNGLoader {

  public static class PNGResult {
    public ByteBuffer bytes;
    public int width;
    public int height;
  }

  public static enum Format {
    ALPHA, LUMINANCE, RGB, RGBA, ABGR;
  }

  public static PNGResult load(String fileName, Format format) {
    PNGDecoder.TextureFormat pngFormat;
    if (format.equals(Format.ABGR)) {
      pngFormat = PNGDecoder.TextureFormat.ABGR;
    } else if (format.equals(Format.ALPHA)) {
      pngFormat = PNGDecoder.TextureFormat.ALPHA;
    } else if (format.equals(Format.LUMINANCE)) {
      pngFormat = PNGDecoder.TextureFormat.LUMINANCE;
    } else if (format.equals(Format.RGB)) {
      pngFormat = PNGDecoder.TextureFormat.RGB;
    } else if (format.equals(Format.RGBA)) {
      pngFormat = PNGDecoder.TextureFormat.RGBA;
    } else {
      throw new IllegalArgumentException("Illegal format given.");
    }
    FileInputStream fileInputStream = null;
    PNGResult result = new PNGResult();
    try {
      fileInputStream = new FileInputStream(fileName);
      PNGDecoder pngDecoder = new PNGDecoder(fileInputStream);
      ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * pngDecoder.getWidth() * pngDecoder.getHeight());
      pngDecoder.decode(byteBuffer, pngDecoder.getWidth() * 4, pngFormat);
      byteBuffer.flip();
      result.bytes = byteBuffer;
      result.width = pngDecoder.getWidth();
      result.height = pngDecoder.getHeight();
    } catch (FileNotFoundException e) {
      System.err.println("Failed to load PNG " + fileName);
      System.exit(-1);
    } catch (IOException e) {
      System.err.println("Failed to load PNG " + fileName);
      System.exit(-1);
    } finally {
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException e) {
          // we tried...
        }
      }
    }
    return result;
  }
}
