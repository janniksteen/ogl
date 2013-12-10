package dk.norgaardsteen.ogl.shader;

import dk.norgaardsteen.ogl.UniformLocation;

import java.util.ArrayList;
import java.util.List;

public class ProgramLinkerResult {
  public int programHandle = -1;
  public List<UniformLocation> uniformLocations = new ArrayList<UniformLocation>();

  public ProgramLinkerResult(int programHandle, List<UniformLocation> uniformLocations) {
    this.programHandle = programHandle;
    this.uniformLocations = uniformLocations;
  }
}