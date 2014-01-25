package dk.norgaardsteen.ogl.mesh.color;

/**
 * User: jns
 * Date: 1/25/14
 * Time: 12:48 AM
 */
public class ColorCollection {
  private static float CF = 1.0f/255.0f; // COLOR_FACTOR

  //http://www.colourlovers.com/palette/98437/Earth_is_Dessert
  public static final float[] DIRT_BROWN = new float[] {
    CF * 158.0f, CF * 116.0f, CF * 44.0f,
    CF * 146.0f, CF * 108.0f, CF * 42.0f,
    CF * 129.0f, CF * 95.0f, CF * 37.0f,
    CF * 114.0f, CF * 84.0f, CF * 32.0f,
    CF * 102.0f, CF * 75.0f, CF * 29.0f
  };

  // http://www.colourlovers.com/palette/800825/Down_to_Earth
  public static final float[] DIRT_LIGHT_DARK_BROWN_SAND = new float[] {
    CF * 226.0f, CF * 217.0f, CF * 166.0f,
    CF * 166.0f, CF * 142.0f, CF * 94.0f,
    CF * 110.0f, CF * 85.0f, CF * 56.0f,
    CF * 64.0f, CF * 47.0f, CF * 24.0f,
    CF * 35.0f, CF * 24.0f, CF * 17.0f
  };

  //http://www.colourlovers.com/palette/123060/Graveyard_Work
  public static final float[] DIRT_LIGHT_BROW_GREY = new float[] {
    CF * 121.0f, CF * 114.0f, CF * 106.0f,
    CF * 129.0f, CF * 114.0f, CF * 93.0f,
    CF * 131.0f, CF * 101.0f, CF * 62.0f,
    CF * 134.0f, CF * 91.0f, CF * 35.0f,
    CF * 133.0f, CF * 77.0f, CF * 5.0f
  };

  //http://www.colourlovers.com/palette/339014/doing_the_dirt
  public static final float[] DIRT_DARK_SWAMP = new float[] {
    CF * 41.0f, CF * 29.0f, CF * 19.0f,
    CF * 41.0f, CF * 34.0f, CF * 19.0f,
    CF * 20.0f, CF * 15.0f, CF * 2.0f,
    CF * 41.0f ,CF * 35.0f, CF * 19.0f,
    CF * 54.0f, CF * 41.0f ,CF * 5.0f
  };

  public static final float[] DIRT_GARDEN = new float[] {
    CF * 75.0f, CF * 45.0f, CF * 11.0f,
    CF * 51.0f, CF * 27.0f, CF * 6.0f,
    CF * 84.0f, CF * 34.0f, CF * 3.0f,
    CF * 109.0f, CF * 68.0f, CF * 10.0f,
    CF * 135.0f, CF * 89.0f, CF * 20.0f
  };

  //http://www.colourlovers.com/palette/1589318/Fresh_Cut_Grass
  public static final float[] FRESH_CUT_GRASS = new float[] {
    CF * 196.0f, CF * 255.0f, CF * 87.0f,
    CF * 114.0f, CF * 163.0f, CF * 21.0f,
    CF * 70.0f, CF * 107.0f, CF * 0.0f,
    CF * 0.0f, CF * 79.0f, CF * 11.0f,
    CF * 0.0f, CF * 43.0f, CF * 17.0f
  };

  //http://www.colourlovers.com/palette/110443/Summer_Grass
  public static final float[] SUMMER_GRASS = new float[] {
    CF * 234.0f, CF * 247.0f, CF * 217.0f,
    CF * 195.0f, CF * 214.0f, CF * 170.0f,
    CF * 142.0f, CF * 168.0f, CF * 108.0f,
    CF * 77.0f, CF * 100.0f, CF * 45.0f,
    CF * 40.0f, CF * 58.0f, CF * 16.0f
  };

  //http://www.colourlovers.com/palette/1005008/grass_stain
  public static final float[] DRY_GRASS = new float[] {
    CF * 242.0f, CF * 200.0f, CF * 102.0f,
    CF * 212.0f, CF * 172.0f, CF * 53.0f,
    CF * 171.0f, CF * 151.0f, CF * 26.0f,
    CF * 133.0f, CF * 131.0f, CF * 12.0f,
    CF * 33.0f, CF * 43.0f, CF * 0.0f
  };

  //http://www.colourlovers.com/palette/10245/the_grass_is_greener
  public static final float[] VERY_GREEN_GRASS = new float[] {
    CF * 77.0f, CF * 172.0f, CF * 39.0f,
    CF * 64.0f, CF * 139.0f, CF * 34.0f,
    CF * 48.0f, CF * 105.0f, CF * 25.0f,
    CF * 38.0f, CF * 82.0f, CF * 20.0f,
    CF * 26.0f, CF * 56.0f, CF * 14.0f
  };
}
