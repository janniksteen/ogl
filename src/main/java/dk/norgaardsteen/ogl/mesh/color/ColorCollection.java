package dk.norgaardsteen.ogl.mesh.color;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jns
 * Date: 1/25/14
 * Time: 12:48 AM
 */
public class ColorCollection {
  public enum Name {
    STONE_DARK,
    GRASS_FRESH_CUT,
    SWAMP_DARK,
    PLAINS_GRASSY,
    DIRT_LIGHT,
    DIRT,
    DIRT_DARK,
  };

  private final static Map<Name, byte[]> colorPalettes = new HashMap<>();

  public static byte[] getColors(Name name) {
    return colorPalettes.get(name);
  }

  //http://www.colourlovers.com/palette/98437/Earth_is_Dessert
  private static final byte[] DIRT_BROWN_RGB = new byte[] {
    (byte)158, (byte)116, (byte)44, (byte)20,
    (byte)146, (byte)108, (byte)42, (byte)20,
    (byte)129, (byte)95, (byte)37, (byte)20,
    (byte)114, (byte)84, (byte)32, (byte)20,
    (byte)102, (byte)75, (byte)29, (byte)20
  };

  //http://www.colourlovers.com/palette/800825/Down_to_Earth
  private static final byte[] DIRT_LIGHT_DARK_BROWN_SAND_RGB = new byte[] {
    (byte)226, (byte)217, (byte)166, (byte)20,
    (byte)166, (byte)142, (byte)94, (byte)20,
    (byte)110, (byte)85, (byte)56, (byte)20,
    (byte)64, (byte)47, (byte)24, (byte)20,
    (byte)35, (byte)24, (byte)17, (byte)20
  };

  //http://www.colourlovers.com/palette/123060/Graveyard_Work
  private static final byte[] DIRT_LIGHT_BROWN_GREY_RGB = new byte[] {
    (byte)121, (byte)114, (byte)106, (byte)20,
    (byte)129, (byte)114, (byte)93, (byte)20,
    (byte)131, (byte)101, (byte)62, (byte)20,
    (byte)134, (byte)91, (byte)35, (byte)20,
    (byte)133, (byte)77, (byte)5, (byte)20
  };

  //http://www.colourlovers.com/palette/339014/doing_the_dirt
  private static final byte[] SWAMP_DARK_RGB = new byte[] {
    (byte)41, (byte)29, (byte)19, (byte)47,
    (byte)41, (byte)34, (byte)19, (byte)16,
    (byte)20, (byte)15, (byte)2, (byte)15,
    (byte)41 ,(byte)35, (byte)19, (byte)5,
    (byte)54, (byte)41 ,(byte)5, (byte)17
  };

  //http://www.colourlovers.com/palette/1859556/The_Ground_is_Brown
  private static final byte[] DIRT_RGB = new byte[] {
    (byte)75, (byte)45, (byte)11, (byte)20,
    (byte)51, (byte)27, (byte)6, (byte)20,
    (byte)84, (byte)34, (byte)3, (byte)20,
    (byte)109, (byte)68, (byte)10, (byte)20,
    (byte)135, (byte)89, (byte)20, (byte)20
  };

  //http://www.colourlovers.com/palette/79174/That_Old_Dirt_Road
  private static final byte[] DIRT_LIGHT_RGB = new byte[] {
    (byte)168, (byte)133, (byte)95, (byte)20,
    (byte)146, (byte)108, (byte)67, (byte)20,
    (byte)128, (byte)86, (byte)42, (byte)20,
    (byte)107, (byte)65, (byte)20, (byte)20,
    (byte)84, (byte)46, (byte)5, (byte)20
  };

  //http://www.colourlovers.com/palette/931074/into_the_dirt
  private static final byte[] DIRT_DARK_RGB = new byte[] {
    (byte)46, (byte)33, (byte)27, (byte)2,
    (byte)43, (byte)29, (byte)23, (byte)2,
    (byte)38, (byte)22, (byte)15, (byte)2,
    (byte)31, (byte)15, (byte)9, (byte)2,
    (byte)23, (byte)9, (byte)3, (byte)92
  };

  //http://www.colourlovers.com/palette/1589318/Fresh_Cut_Grass
  private static final byte[] GRASS_FRESH_CUT_RGB = new byte[] {
    (byte)196, (byte)255, (byte)87, (byte)21,
    (byte)114, (byte)163, (byte)21, (byte)3,
    (byte)70, (byte)107, (byte)0, (byte)5,
    (byte)0, (byte)79, (byte)11, (byte)12,
    (byte)0, (byte)43, (byte)17, (byte)59
  };

  //http://www.colourlovers.com/palette/110443/Summer_Grass
  private static final byte[] GRASS_SUMMER_RGB = new byte[] {
    (byte)234, (byte)247, (byte)217, (byte)20,
    (byte)195, (byte)214, (byte)170, (byte)20,
    (byte)142, (byte)168, (byte)108, (byte)20,
    (byte)77, (byte)100, (byte)45, (byte)20,
    (byte)40, (byte)58, (byte)16, (byte)20
  };

  //http://www.colourlovers.com/palette/1005008/grass_stain
  private static final byte[] GRASS_DRY_RGB = new byte[] {
    (byte)242, (byte)200, (byte)102, (byte)20,
    (byte)212, (byte)172, (byte)53, (byte)20,
    (byte)171, (byte)151, (byte)26, (byte)20,
    (byte)133, (byte)131, (byte)12, (byte)20,
    (byte)33, (byte)43, (byte)0, (byte)20
  };

  //http://www.colourlovers.com/palette/10245/the_grass_is_greener
  private static final byte[] GRASS_VERY_GREEN_RGB = new byte[] {
    (byte)77, (byte)172, (byte)39, (byte)20,
    (byte)64, (byte)139, (byte)34, (byte)20,
    (byte)48, (byte)105, (byte)25, (byte)20,
    (byte)38, (byte)82, (byte)20, (byte)20,
    (byte)26, (byte)56, (byte)14, (byte)20
  };

  //http://www.colourlovers.com/palette/80134/clovered_plains
  private static final byte[] PLAINS_CLOVERED_RGB = new byte[] {
    (byte)243, (byte)233, (byte)225, (byte)20,
    (byte)22, (byte)102, (byte)61, (byte)20,
    (byte)103, (byte)143, (byte)80, (byte)20,
    (byte)189, (byte)196, (byte)108, (byte)20,
    (byte)223, (byte)213, (byte)162, (byte)20
  };

  //http://www.colourlovers.com/palette/391441/Flood_Plain
  private static final byte[] PLAINS_FLOOD_RGB = new byte[] {
    (byte)7, (byte)46, (byte)19, (byte)20,
    (byte)10, (byte)64, (byte)26, (byte)20,
    (byte)22, (byte)89, (byte)42, (byte)20,
    (byte)50, (byte)128, (byte)73, (byte)20,
    (byte)101, (byte)163, (byte)120, (byte)20
  };

  //http://www.colourlovers.com/palette/648207/Emerald_Grassy_Plain
  private static final byte[] PLAINS_GRASSY_RGB = new byte[] {
    (byte)70, (byte)82, (byte)6, (byte)65,
    (byte)235, (byte)211, (byte)203, (byte)3,
    (byte)75, (byte)75, (byte)87, (byte)18,
    (byte)145, (byte)156, (byte)64, (byte)3,
    (byte)68, (byte)158, (byte)17, (byte)11
  };

    //http://www.colourlovers.com/palette/269469/Stormy_Seashore
  private static final byte[] STONE_STORMY_SEASHORE_RGB = new byte[] {
    (byte)91, (byte)105, (byte)106, (byte)20,
    (byte)113, (byte)131, (byte)132, (byte)20,
    (byte)222, (byte)222, (byte)203, (byte)20,
    (byte)199, (byte)199, (byte)165, (byte)20,
    (byte)163, (byte)162, (byte)137, (byte)20
  };

  //http://www.colourlovers.com/palette/2808706/DARK_STONE
  private static final byte[] STONE_DARK_RGB = new byte[] {
    (byte)72, (byte)72, (byte)72, (byte)64,
    (byte)96, (byte)91, (byte)87, (byte)7,
    (byte)151, (byte)141, (byte)129, (byte)6,
    (byte)173, (byte)165, (byte)154, (byte)3,
    (byte)189, (byte)187, (byte)166, (byte)20
  };

  static {
    colorPalettes.put(Name.STONE_DARK, STONE_DARK_RGB);
    colorPalettes.put(Name.GRASS_FRESH_CUT, GRASS_FRESH_CUT_RGB);
    colorPalettes.put(Name.PLAINS_GRASSY, PLAINS_GRASSY_RGB);
    colorPalettes.put(Name.SWAMP_DARK, SWAMP_DARK_RGB);
    colorPalettes.put(Name.DIRT_LIGHT, DIRT_LIGHT_RGB);
    colorPalettes.put(Name.DIRT, DIRT_RGB);
    colorPalettes.put(Name.DIRT_DARK, DIRT_DARK_RGB);
  }
}