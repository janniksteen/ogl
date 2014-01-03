package dk.norgaardsteen.ogl;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * User: jns
 * Date: 11/1/13
 * Time: 11:32 AM
 */
public abstract class Base implements Lifecycle {
  private int fps;
  protected long lastFPS;
  protected long lastPosition;

  public void start() throws LWJGLException {
    System.out.println("Started.");
    init();
    prepareProgram();
    prepareShaders();
    prepareBuffers();
    prepareTextures();
    prepareMatrices();
    lastFPS = getTime();
    lastPosition = getTime();

    while (!Display.isCloseRequested()) {
      input();
      beforeRender();
      updateFPS();
      render();
      //Display.sync(60); // cap fps to 60fps
      Display.update();
    }
  }

  protected abstract void cleanup();
  protected abstract void updateStats(int fps);

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
  private long getTime() {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }

  /**
   * Calculate the FPS and set it in the title bar
   */
  private void updateFPS() {
    if (getTime() - lastFPS > 1000) {
      updateStats(fps);
      fps = 0;
      lastFPS += 1000;
    }
    fps++;
  }

  protected void hhandleErrors() {
    int glError = GL11.glGetError();
    if(glError != GL11.GL_NO_ERROR)
    {
      throw new RuntimeException("OpenGL error: " + GLU.gluErrorString(glError));
    }
  }
}