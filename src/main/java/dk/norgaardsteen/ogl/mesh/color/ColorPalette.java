package dk.norgaardsteen.ogl.mesh.color;

import dk.norgaardsteen.ogl.resource.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jns
 * Date: 2/5/14
 * Time: 11:23 AM
 */
public class ColorPalette {
  private ColorCollection.Name name;
  private boolean isSane = false;
  private List<ColorDescription> colorDescriptions = new ArrayList<>();

  public ColorPalette(ColorCollection.Name name, byte[] colors, boolean opaque) {
    this.name = name;
    for (int i = 0; i < colors.length; i += 4) {
      addColor(colors[i], colors[i + 1], colors[i + 2], (byte)(opaque ? 1 : 0), ((float)colors[i + 3] / 100.0f));
    }
  }

  private void addColor(byte r, byte g, byte b, byte a, float w) {
    this.colorDescriptions.add(new ColorDescription(r, g, b, a, w));
  }

  public ColorDescription getWeighedRandomColor() {
    if (!isSane) {
      boolean isWeightsSane = areColorWeightsSane();
      if (!isWeightsSane) {
        throw new RuntimeException("ColorPalette " + name + " has insane color weights");
      } else {
        isSane = true;
      }
    }

    float sum = 0;
    float r = ApplicationContext.getRandomFloat();
    for (ColorDescription c : colorDescriptions) {
      sum += c.getW();
      if (r <= sum) {
        return c;
      }
    }
    throw new RuntimeException("No color descriptions avaliable!");
  }

  private boolean areColorWeightsSane() {
    float sum = 0.0f;
    for (ColorDescription c : colorDescriptions) {
      sum += c.getW();
    }
    return sum <= 1.0f;
  }
}