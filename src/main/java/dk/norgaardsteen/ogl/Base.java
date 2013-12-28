package dk.norgaardsteen.ogl;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL20;

import java.util.Collection;

/**
 * User: jns
 * Date: 11/1/13
 * Time: 11:32 AM
 */
public abstract class Base implements Lifecycle {
  protected int fps;
  protected long lastFrame;
  protected long lastFPS;

  public void start() throws LWJGLException {
    System.out.println("Started.");
    init();
    prepareProgram();
    prepareShaders();
    prepareBuffers();
    prepareTextures();
    prepareMatrices();
    getDelta();
    lastFPS = getTime();
    while (!Display.isCloseRequested()) {
      input();
      beforeRender();
      render();
      updateFPS();
      //Display.sync(60); // cap fps to 60fps
      Display.update();
    }
  }

  protected abstract void cleanup();

  public void stop() {
    cleanup();
    Display.destroy();
    System.out.println("Stopped.");
  }

  /**
   * Get the accurate system time
   *
   * @return The system time in milliseconds
   */
  protected long getTime() {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }

  public int getDelta() {
    long time = getTime();
    int delta = (int) (time - lastFrame);
    lastFrame = time;
    return delta;
  }
  /**
   * Calculate the FPS and set it in the title bar
   */
  protected void updateFPS() {
    if (getTime() - lastFPS > 1000) {
      Display.setTitle("FPS: " + fps);
      fps = 0;
      lastFPS += 1000;
    }
    fps++;
  }
}