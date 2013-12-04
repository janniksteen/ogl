package dk.norgaardsteen.ogl.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * User: jns
 * Date: 11/2/13
 * Time: 6:10 PM
 */
public class ShaderCompiler {

  /**
   * @param fileName to the shader file
   * @param shaderType GL20.GL_VERTEX_SHADER or GL20.GL_FRAGMENT_SHADER
   * @return
   */
  public static int loadAndCompileShader(String fileName, int shaderType) {
    int shaderHandle = GL20.glCreateShader(shaderType);
    if (shaderHandle == 0) {
      throw new RuntimeException("Failed to create shader (" + getShaderTypeString(shaderType) + ")");
    }
    // load and prepareShaders shader
    String shaderCode = loadShaderCode(fileName);
    GL20.glShaderSource(shaderHandle, shaderCode);
    System.out.println("Compiling " + fileName + "...");
    GL20.glCompileShader(shaderHandle);

    int compileStatus = GL20.glGetShaderi(shaderHandle, GL20.GL_COMPILE_STATUS);
    if (compileStatus == GL11.GL_FALSE) {
      // check prepareShaders result
      int infoLogLength = GL20.glGetShaderi(shaderHandle, GL20.GL_INFO_LOG_LENGTH);
      String infoLog = GL20.glGetShaderInfoLog(shaderHandle, infoLogLength);
      throw new RuntimeException("Compile shader had an error: : " + infoLog);
    } else {
      System.out.println("Shader compiled OK.");
    }

    return shaderHandle;
  }

  private static String loadShaderCode(String filename) {
    StringBuilder shaderSourceCode = new StringBuilder();
    String line = null ;
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      while( (line = reader.readLine()) !=null ) {
        shaderSourceCode.append(line);
        shaderSourceCode.append('\n');
      }
    }
    catch(Exception e) {
      throw new IllegalArgumentException("Unable to load shader source code from file ["+filename+"]", e);
    }

    return shaderSourceCode.toString();
  }

  private static String getShaderTypeString(int shaderType) {
    switch (shaderType) {
      case GL20.GL_VERTEX_SHADER:
        return "GL_VERTEX_SHADER";
      case GL20.GL_FRAGMENT_SHADER:
        return "GL_FRAGMENT_SHADER";
      default:
        return "UNKNOWN SHADER";
    }
  }
}
