package dk.norgaardsteen.ogl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.List;

/**
 * User: jns
 * Date: 11/4/13
 * Time: 9:20 PM
 */
public class ProgramLinker {

  public static ProgramLinkerResult link(int[] shaderHandles, List<AttribLocation> attribLocations, List<UniformLocation> uniformLocations) {
    int programHandle = GL20.glCreateProgram();
    System.out.println("Created program.");
    for (int i = 0; i < shaderHandles.length; i++) {
      GL20.glAttachShader(programHandle, shaderHandles[i]);
    }
    for (AttribLocation attribLocation : attribLocations) {
      GL20.glBindAttribLocation(programHandle, attribLocation.idx, attribLocation.name);
    }
    GL20.glLinkProgram(programHandle);
    GL20.glValidateProgram(programHandle);
    int linkStatus = GL20.glGetProgrami(programHandle, GL20.GL_LINK_STATUS);
    if (linkStatus == GL11.GL_FALSE) {
      int infoLogLength = GL20.glGetProgrami(programHandle, GL20.GL_INFO_LOG_LENGTH);
      String infoLog = GL20.glGetProgramInfoLog(programHandle, infoLogLength);
      throw new RuntimeException("Link failed with an error: " + infoLog);
    } else {
      System.out.println("Program linked OK.");
    }

    for (UniformLocation uniformLocation : uniformLocations) {
      uniformLocation.handle = GL20.glGetUniformLocation(programHandle, uniformLocation.name);
    }

    // clean up
    for (int i = 0; i < shaderHandles.length; i++) {
      GL20.glDeleteShader(shaderHandles[i]);
    }

    return new ProgramLinkerResult(programHandle, uniformLocations);
  }
}