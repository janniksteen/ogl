package dk.norgaardsteen.ogl.app;

import dk.norgaardsteen.ogl.mesh.DeprecatedShape;
import dk.norgaardsteen.ogl.shader.shared.AttribLocation;
import dk.norgaardsteen.ogl.Base;
import dk.norgaardsteen.ogl.shader.shared.UniformLocation;
import dk.norgaardsteen.ogl.math.Trigonometric;
import dk.norgaardsteen.ogl.mesh.Quad;
import dk.norgaardsteen.ogl.shader.ProgramLinker;
import dk.norgaardsteen.ogl.shader.ProgramLinkerResult;
import dk.norgaardsteen.ogl.shader.ShaderCompiler;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jns
 * Date: 11/1/13
 * Time: 11:51 AM
 */
public class Quad3D extends Base {

  protected static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
  protected static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
  protected static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

  protected final int displayWidth = 1280;
  protected final int displayHeight = 720;

  protected int vaoHandle;
  protected int verticesVBOHandle;
  protected int colorVBOHandle;

  private int[] programIDs;

  protected int projectionMatrixUniformLocationHandle;
  protected int viewMatrixUniformLocationHandle;
  protected int modelMatrixUniformLocationHandle;

  protected final FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16);

  protected static final String VERTEX_SHADER_FILE = "src/main/resources/shaders/simple.vsh";
  protected static final String FRAGMENT_SHADER_FILE = "src/main/resources/shaders/simple.fsh";

  private final DeprecatedShape shape = new Quad();
  private float vertices[];

  private Vector3f modelScale = new Vector3f(0.5f, 0.5f, 0.0f);
  private Vector3f modelPosition = new Vector3f(0.0f, 0.0f, 0.0f);
  private Vector3f modelRotation = new Vector3f(0.0f, 0.0f, 0.0f);

  private Vector3f cameraPosition = new Vector3f(0.0f, 0.0f, -1.0f);
  private Vector3f cameraRotation = new Vector3f(0.0f, 0.0f, 0.0f);
  private final float cameraRotationDelta = 3f;

  private float fieldOfView = 67.0f;
  private final float fieldOfViewDelta = 1.0f;

  private final float rotationDelta = 5f;
  private final float scaleDelta = 0.05f;
  private final float positionDelta = 0.05f;
  private final Vector3f scaleAddResolution = new Vector3f(scaleDelta, scaleDelta, scaleDelta);
  private final Vector3f scaleMinusResolution = new Vector3f(-scaleDelta, -scaleDelta, -scaleDelta);

  @Override
  public void prepareBuffers() {
    vertices = shape.vertices();
    // convert vertices array to buffer
    FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
    verticesBuffer.put(vertices);
    verticesBuffer.flip();
    verticesVBOHandle = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticesVBOHandle);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

    // convert color array to buffer

    float[] colors = shape.colors();
    FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colors.length);
    colorBuffer.put(colors);
    colorBuffer.flip();
    colorVBOHandle = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorVBOHandle);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);

    // unbind!
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    // create vertex array object (VAO) - an object that describes the data set
    vaoHandle = GL30.glGenVertexArrays();
    GL30.glBindVertexArray(vaoHandle);
    GL20.glEnableVertexAttribArray(0);
    GL20.glEnableVertexAttribArray(1);

    // assign vertices VBO to index 0 of VAO
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticesVBOHandle);
    // and describe it:
    // * 1: array index (in VAO)
    // * 2: number of values pr. vertex
    // * 3: value type
    // * 4: normalized or not
    // * 5: stride (spacing between values)
    // * 6: offset to values from the start of array (idx=0)
    GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);

    // assign color VBO to index 1 of VAO
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorVBOHandle);
    GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

    // unbind!
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
  }

  @Override
  public void prepareTextures() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void prepareMatrices() {
    if (vertices == null) {
      throw new RuntimeException("No vertices defined!");
    }

    // position camera in view
    Matrix4f viewMatrix = new Matrix4f();
    Matrix4f.translate(cameraPosition, viewMatrix, viewMatrix);
    Matrix4f.rotate((float)Math.toRadians(0), Z_AXIS, viewMatrix, viewMatrix);
    Matrix4f.rotate((float)Math.toRadians(cameraRotation.y), Y_AXIS, viewMatrix, viewMatrix);
    Matrix4f.rotate((float)Math.toRadians(cameraRotation.x), X_AXIS, viewMatrix, viewMatrix);

    // Setup projection matrix
    Matrix4f projectionMatrix = new Matrix4f();
    float aspectRatio = (float)displayWidth / (float)displayHeight;
    float near_plane = 0.1f;
    float far_plane = 500f;

    float y_scale = Trigonometric.coTangent((float) Math.toRadians((double) (fieldOfView / 2f)));
    float x_scale = y_scale / aspectRatio;
    float frustum_length = far_plane - near_plane;

    projectionMatrix.m00 = x_scale;
    projectionMatrix.m11 = y_scale;
    projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
    projectionMatrix.m23 = -1;
    projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
    projectionMatrix.m33 = 0;

    // scale, translate, rotate model
    Matrix4f modelMatrix = new Matrix4f();
    Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
    Matrix4f.translate(modelPosition, modelMatrix, modelMatrix);
    Matrix4f.rotate((float)Math.toRadians(modelRotation.z), Z_AXIS, modelMatrix, modelMatrix);
    Matrix4f.rotate((float)Math.toRadians(modelRotation.y), Y_AXIS, modelMatrix, modelMatrix);
    Matrix4f.rotate((float)Math.toRadians(modelRotation.x), X_AXIS, modelMatrix, modelMatrix);

//    GL20.glUseProgram(programIDs[0]);

    projectionMatrix.store(matrix4fBuffer);
    matrix4fBuffer.flip();
//    GL20.glUniformMatrix4(projectionMatrixUniformLocationHandle, false, matrix4fBuffer);

    viewMatrix.store(matrix4fBuffer);
    matrix4fBuffer.flip();
//    GL20.glUniformMatrix4(viewMatrixUniformLocationHandle, false, matrix4fBuffer);

    modelMatrix.store(matrix4fBuffer);
    matrix4fBuffer.flip();
//    GL20.glUniformMatrix4(modelMatrixUniformLocationHandle, false, matrix4fBuffer);

//    GL20.glUseProgram(0);
  }

  @Override
  public void input() {
    while (Keyboard.next()) {
      // Only listen to events where the key was pressed (down event)
      if (!Keyboard.getEventKeyState()) {
        continue;
      }

      switch (Keyboard.getEventKey()) {
        // Move
        case Keyboard.KEY_UP:
          modelPosition.y += positionDelta;
          break;
        case Keyboard.KEY_DOWN:
          modelPosition.y -= positionDelta;
          break;

        // Scale
        case Keyboard.KEY_ADD:
          Vector3f.add(modelScale, scaleAddResolution, modelScale);
          break;
        case Keyboard.KEY_MINUS:
          Vector3f.add(modelScale, scaleMinusResolution, modelScale);
          break;

        // Z rotation
        case Keyboard.KEY_LEFT:
          modelRotation.z += rotationDelta;
          break;
        case Keyboard.KEY_RIGHT:
          modelRotation.z -= rotationDelta;
          break;

        // Y rotation
        case Keyboard.KEY_A:
          modelRotation.y += rotationDelta;
          break;
        case Keyboard.KEY_D:
          modelRotation.y -= rotationDelta;
          break;

        // X rotation
        case Keyboard.KEY_W:
          modelRotation.x += rotationDelta;
          break;
        case Keyboard.KEY_S:
          modelRotation.x -= rotationDelta;
          break;

        // camera Y rotation
        case Keyboard.KEY_J:
          cameraRotation.y += cameraRotationDelta;
          break;
        case Keyboard.KEY_L:
          cameraRotation.y -= cameraRotationDelta;
          break;

        // camera X rotation
        case Keyboard.KEY_I:
          cameraRotation.x += cameraRotationDelta;
          break;
        case Keyboard.KEY_K:
          cameraRotation.x -= cameraRotationDelta;
          break;

        // field of view
        case Keyboard.KEY_F1:
          if (fieldOfView + fieldOfViewDelta <= 80) {
            fieldOfView += fieldOfViewDelta;
          }
          break;
        case Keyboard.KEY_F2:
          if (fieldOfView - fieldOfViewDelta >= 20) {
            fieldOfView -= fieldOfViewDelta;
          }
          break;

        // reset
        case Keyboard.KEY_ESCAPE:
          modelScale = new Vector3f(0.5f, 0.5f, 0.0f);
          modelPosition = new Vector3f(0.0f, 0.0f, 0.0f);
          modelRotation = new Vector3f(0.0f, 0.0f, 0.0f);
          cameraPosition = new Vector3f(0.0f, 0.0f, -1.0f);
          cameraRotation = new Vector3f(0.0f, 0.0f, 0.0f);
          fieldOfView = 45.0f;
          break;
      }
    }
  }

  @Override
  public void beforeRender() {

  }

  @Override
  public void prepareProgram() {
    int vertexShaderHandle = ShaderCompiler.loadAndCompileShader(VERTEX_SHADER_FILE, GL20.GL_VERTEX_SHADER);
    int fragmentShaderHandle = ShaderCompiler.loadAndCompileShader(FRAGMENT_SHADER_FILE, GL20.GL_FRAGMENT_SHADER);

    List<AttribLocation> attribLocationList = new ArrayList<AttribLocation>(2);
    AttribLocation inPositionAttribLocation = new AttribLocation(0, "in_position");
    AttribLocation inColorAttribLocation = new AttribLocation(1, "in_color");
    attribLocationList.add(inPositionAttribLocation);
    attribLocationList.add(inColorAttribLocation);

    List<UniformLocation> uniformLocationList = new ArrayList<UniformLocation>(3);
    UniformLocation projectionMatrixUniformLocation = new UniformLocation(UniformLocation.PROJECTION_MATRIX_UNIFORM_LOCATION_NAME);
    UniformLocation viewMatrixUniformLocation = new UniformLocation(UniformLocation.VIEW_MATRIX_UNIFORM_LOCATION_NAME);
    UniformLocation modelMatrixUniformLocation = new UniformLocation(UniformLocation.MODEL_MATRIX_UNIFORM_LOCATION_NAME);
    uniformLocationList.add(projectionMatrixUniformLocation);
    uniformLocationList.add(viewMatrixUniformLocation);
    uniformLocationList.add(modelMatrixUniformLocation);

    ProgramLinkerResult programLinkerResult = ProgramLinker.link(new int[]{vertexShaderHandle, fragmentShaderHandle}, attribLocationList, uniformLocationList);

    for(UniformLocation uniformLocation : programLinkerResult.getUniformLocations()) {
      if (uniformLocation.name.equals(UniformLocation.PROJECTION_MATRIX_UNIFORM_LOCATION_NAME)) {
        projectionMatrixUniformLocationHandle = uniformLocation.handle;
      } else if (uniformLocation.name.equals(UniformLocation.VIEW_MATRIX_UNIFORM_LOCATION_NAME)) {
        viewMatrixUniformLocationHandle = uniformLocation.handle;
      } else if (uniformLocation.name.equals(UniformLocation.MODEL_MATRIX_UNIFORM_LOCATION_NAME)) {
        modelMatrixUniformLocationHandle = uniformLocation.handle;
      } else
        throw new RuntimeException("Unknown uniform location name: " + uniformLocation.name);
    }

    programIDs = new int[]{programLinkerResult.programHandle};
  }

  @Override
  public void render() {
    // Clear The Screen And The Depth Buffer
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

    GL20.glUseProgram(programIDs[0]);

    GL20.glUniformMatrix4(projectionMatrixUniformLocationHandle, false, matrix4fBuffer);
    GL20.glUniformMatrix4(viewMatrixUniformLocationHandle, false, matrix4fBuffer);
    GL20.glUniformMatrix4(modelMatrixUniformLocationHandle, false, matrix4fBuffer);

    // enable the VAO
    GL30.glBindVertexArray(vaoHandle);
    GL20.glEnableVertexAttribArray(0);
    GL20.glEnableVertexAttribArray(1);
    // createTextTiles the shape:
    // 1: type of shape to createTextTiles
    // 2: start index of the vertex array
    // 3: number of values for each vertex
    GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, shape.vertices().length / shape.vertexPositionElementCount());

    if(GL11.glGetError() != GL11.GL_NO_ERROR)
    {
      throw new RuntimeException("OpenGL error: " + GLU.gluErrorString(GL11.glGetError()));
    }

    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL30.glBindVertexArray(0);
    GL20.glUseProgram(0);
  }

  @Override
  public void init() {
    try {
      Display.setTitle(Quad3D.class.getName());
      Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
      Keyboard.enableRepeatEvents(true);

      // set up OpenGL to run in forward-compatible mode
      // so that using deprecated functionality will
      // throw an error. This ensures that we are making
      // use of OpenGL 3.3
      PixelFormat pixelFormat = new PixelFormat();
      ContextAttribs contextAttributes = new ContextAttribs(3, 3);
      contextAttributes.withForwardCompatible(true);
      contextAttributes.withProfileCore(true);
      Display.create(pixelFormat, contextAttributes);

      // set viewport and blank color
      GL11.glViewport(0, 0, displayWidth, displayHeight);
      GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glDepthFunc(GL11.GL_LESS);
    } catch (LWJGLException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  protected void cleanup() {
    GL30.glBindVertexArray(vaoHandle);
    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GL15.glDeleteBuffers(verticesVBOHandle);
    GL15.glDeleteBuffers(colorVBOHandle);
    GL30.glBindVertexArray(0);
    GL30.glDeleteVertexArrays(vaoHandle);
    System.out.println("Cleaned up.");
  }

  public static void main(String[] args) throws LWJGLException {
    Quad3D fun = new Quad3D();
    fun.start();
    fun.stop();
  }
}