package dk.norgaardsteen.ogl.texture;

import dk.norgaardsteen.ogl.mesh.color.ColorCollection;
import dk.norgaardsteen.ogl.mesh.color.ColorDescription;
import dk.norgaardsteen.ogl.mesh.color.ColorPalette;
import dk.norgaardsteen.ogl.resource.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jns
 * Date: 1/27/14
 * Time: 8:44 PM
 */
public class Textures {
  public static Map<ColorCollection.Name, TextureData> textureCache = new HashMap<>();

  private static TextureData getTextureData(ColorCollection.Name name) {
    TextureData td = textureCache.get(name);
    if (td == null) {
      ColorPalette colorPalette = new ColorPalette(name, ColorCollection.getColors(name), true);
      td = buildTexture16X16(colorPalette);
      textureCache.put(name, td);
    }
    return td;
  }

  public static TextureData buildStoneDark() {
    return getTextureData(ColorCollection.Name.STONE_DARK);
  }

  public static TextureData buildPlainsGrassy() {
    return getTextureData(ColorCollection.Name.PLAINS_GRASSY);
  }

  public static TextureData buildGrassFreshCut() {
    return getTextureData(ColorCollection.Name.GRASS_FRESH_CUT);
  }

  public static TextureData buildSwampDark() {
    return getTextureData(ColorCollection.Name.SWAMP_DARK);
  }

  public static TextureData buildDirtLight() {
    return getTextureData(ColorCollection.Name.DIRT_LIGHT);
  }

  public static TextureData buildDirt() {
    return getTextureData(ColorCollection.Name.DIRT);
  }

  public static TextureData buildDirtDark() {
    return getTextureData(ColorCollection.Name.DIRT_DARK);
  }

  private static TextureData buildTexture16X16(ColorPalette palette) {
    return buildTexture(palette, 16, 16);
  }

  private static TextureData buildTexture(ColorPalette colorPalette, int w, int h) {
    int c = 4;
    byte[] colors = new byte[w * h * c];

    for (int row = 0; row < h; row++) {
      for (int col = 0; col < w; col++) {
        ColorDescription cd = colorPalette.getWeighedRandomColor();
        colors[(row * w * c) + (col * c) + 0] = cd.getR();
        colors[(row * w * c) + (col * c) + 1] = cd.getG();
        colors[(row * w * c) + (col * c) + 2] = cd.getB();
        colors[(row * w * c) + (col * c) + 3] = cd.getA();
      }
    }
    return new TextureData(colors, w, h);
  }
}