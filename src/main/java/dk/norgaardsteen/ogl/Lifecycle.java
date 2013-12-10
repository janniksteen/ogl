package dk.norgaardsteen.ogl;

/**
 * User: jns
 * Date: 11/4/13
 * Time: 8:38 PM
 */
public interface Lifecycle {
  /** main entry point to the application **/
  public void start();

  /** set up display, view port etc. **/
  public void init();

  /** prepare vertices arrays etc **/
  public void prepareBuffers();

  /** prepare textures **/
  public void prepareTextures();

  /** prepare the various matrices and load in shader **/
  public void prepareMatrices();

  /** receive input (keyboard, mouse, etc.) **/
  public void input();

  /** load, compile and link shader programs
   * @return an array of shader handles
   */
  public int[] prepareShaders();

  /** link shader code to program **/
  public int prepareProgram(int[] shaderHandles);

  /** render (OpenGL draws) the scene **/
  public void render();

  /** clean up **/
  public void stop();
}
