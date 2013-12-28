package dk.norgaardsteen.ogl;

import org.lwjgl.LWJGLException;

/*
 * User: jns
 * Date: 11/4/13
 * Time: 8:38 PM
 */
public interface Lifecycle {
  /* main entry point to the application */
  public void start() throws LWJGLException;

  /* set up display, view port etc. */
  public void init();

  /* prepare vertices arrays etc */
  public void prepareBuffers();

  /* prepare textures */
  public void prepareTextures();

  /* prepare the various matrices */
  public void prepareMatrices();

  /* load, compile and link shader to programs */
  public void prepareProgram();

  /* initialize shaders */
  public void prepareShaders();

  /* receive input (keyboard, mouse, etc.) */
  public void input();

  /* prepare for rendering */
  public void beforeRender();

  /* render (OpenGL draws) the scene */
  public void render();

  /* clean up */
  public void stop();
}
