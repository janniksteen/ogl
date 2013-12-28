package dk.norgaardsteen.ogl.shader;

import dk.norgaardsteen.ogl.shader.shared.UniformLocation;

import java.util.ArrayList;
import java.util.Collection;

public class ProgramLinkerResult {
  public int programHandle = -1;
  private Collection<Integer> shaderHandles;
  private Collection<UniformLocation> uniformLocations;

  public ProgramLinkerResult() {
  }

  public void setProgramHandle(int programHandle) {
    this.programHandle = programHandle;
  }

  public void addUniformLocation(UniformLocation uniformLocation) {
    if (uniformLocations == null) {
      uniformLocations = new ArrayList<UniformLocation>();
    }
    uniformLocations.add(uniformLocation);

  }

  public void addShaderHandle(int shaderHandle) {
    if (shaderHandles == null) {
      shaderHandles = new ArrayList<Integer>();
    }
    shaderHandles.add(new Integer(shaderHandle));
  }

  public Collection<UniformLocation> getUniformLocations() {
    return uniformLocations;
  }

  public int[] getShaderHandles() {
    int[] result = new int[shaderHandles.size()];
    int idx = 0;
    for (Integer shaderHandle : shaderHandles) {
      result [idx++] = shaderHandle.intValue();
    }
    return result;
  }
}