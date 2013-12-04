package dk.norgaardsteen.ogl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * User: jns
 * Date: 10/28/13
 * Time: 10:36 AM
 */
public class VerifyGLEnvironment {
  public VerifyGLEnvironment(){
    try {
      Display.setDisplayMode(new DisplayMode(800, 600));
    } catch (LWJGLException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args){
    new VerifyGLEnvironment();
  }
}
