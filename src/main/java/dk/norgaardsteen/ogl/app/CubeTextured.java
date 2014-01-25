package dk.norgaardsteen.ogl.app;

import dk.norgaardsteen.ogl.Base;
import dk.norgaardsteen.ogl.math.Trigonometric;
import dk.norgaardsteen.ogl.mesh.Cube;
import dk.norgaardsteen.ogl.mesh.Shape;
import dk.norgaardsteen.ogl.mesh.Vertex;
import dk.norgaardsteen.ogl.shader.ProgramLinker;
import dk.norgaardsteen.ogl.shader.ProgramLinkerResult;
import dk.norgaardsteen.ogl.shader.ShaderCompiler;
import dk.norgaardsteen.ogl.shader.shared.AttribLocation;
import dk.norgaardsteen.ogl.shader.shared.UniformLocation;
import dk.norgaardsteen.ogl.util.FloatArrayList;
import dk.norgaardsteen.ogl.util.PNGLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: jns
 * Date: 11/1/13
 * Time: 11:51 AM
 */
public class CubeTextured extends Base {

  private static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
  private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
  private static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

  private final int displayWidth = 1280;
  private final int displayHeight = 720;

  private int vaoHandle;
  private int vboHandle;
  private int vboiHandle;
  private int textureHandle;

  private int[] programIDs;

  private int projectionMatrixUniformLocationHandle;
  private int viewMatrixUniformLocationHandle;
  private int modelMatrixUniformLocationHandle;

  private final FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16);

  private static final String VERTEX_SHADER_FILE = "src/main/resources/shaders/cube_textured.vsh";
  private static final String FRAGMENT_SHADER_FILE = "src/main/resources/shaders/cube_textured.fsh";

  private final Shape shape = new Cube(0.5f);
  private float vertexPositions[];

  private Vector3f modelScale = new Vector3f(0.5f, 0.5f, 0.0f);
  private Vector3f modelPosition = new Vector3f(0.0f, 0.0f, 0.0f);
  private Vector3f modelRotation = new Vector3f(0.0f, 0.0f, 0.0f);

  private Vector3f viewPosition = new Vector3f(0.0f, 0.0f, -1.0f);

  private float fieldOfView = 67.0f;
  private final float fieldOfViewDelta = 1.0f;

  private final float rotationDelta = 5f;
  private final float scaleDelta = 0.05f;
  private final float positionDelta = 0.05f;
  private final Vector3f scaleAddResolution = new Vector3f(scaleDelta, scaleDelta, scaleDelta);
  private final Vector3f scaleMinusResolution = new Vector3f(-scaleDelta, -scaleDelta, -scaleDelta);

  private final static String FONT_TEXTURE_ATLAS_FILE = "src/main/resources/img/uc0x20_0xff_Ubuntu Mono_x10_y14.png";
  private final static String FONT_DESCRIPTION_FILE = "src/main/resources/img/uc0x20_0xff_Ubuntu Mono_x10_y14.fnt";

  @Override
  public void prepareBuffers() {
    // convert vertexPositions array to buffer
    Collection<Vertex> vertexList = shape.getVertices();
    FloatBuffer modelBuffer = BufferUtils.createFloatBuffer(vertexList.size() * Vertex.TOTAL_ELEMENT_COUNT);
    FloatArrayList positionFloatArrayList = new FloatArrayList();
    for (Vertex vertex : vertexList) {
      modelBuffer.put(vertex.getXYZW());
      modelBuffer.put(vertex.getRGBA());
      modelBuffer.put(vertex.getST());
      positionFloatArrayList.put(vertex.getXYZW());
    }
    modelBuffer.flip();
    vertexPositions = positionFloatArrayList.array();

    vaoHandle = GL30.glGenVertexArrays();
    GL30.glBindVertexArray(vaoHandle);

    vboHandle = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboHandle);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, modelBuffer, GL15.GL_STATIC_DRAW);
    // describe it:
    // * 1: array index (in VAO)
    // * 2: number of values pr. vertex
    // * 3: value type
    // * 4: normalized or not
    // * 5: stride (spacing between values) (float size in bytes * total floats per vertex)
    // * 6: offset to values from the start of array (idx=0)
    int stride = Vertex.TOTAL_SIZE_IN_BYTES;
    int colorValuesOffset = Vertex.ELEMENT_BYTES * Vertex.POSITION_ELEMENT_COUNT;
    int textureValueOffset = colorValuesOffset + Vertex.ELEMENT_BYTES * Vertex.COLOR_ELEMENT_COUNT;
    // assign positions
    GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, stride, 0);
    // assign colors
    GL20.glVertexAttribPointer(1, Vertex.COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, stride, colorValuesOffset);
    // assign texture coordinates
    GL20.glVertexAttribPointer(2, Vertex.TEXTURE_COORDINATE_ELEMENT_COUNT, GL11.GL_FLOAT, false, stride, textureValueOffset);

    ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(shape.getIndices().length);
    indicesBuffer.put(shape.getIndices());
    indicesBuffer.flip();

    vboiHandle = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboiHandle);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

    // unbind!
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GL30.glBindVertexArray(0);
  }

  @Override
  public void prepareTextures() {
    PNGLoader.PNGResult pngResult = PNGLoader.load(FONT_TEXTURE_ATLAS_FILE, PNGLoader.Format.RGBA);
    textureHandle = GL11.glGenTextures();
    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
    // rgb bytes aligned and one byte each
    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
    // upload bytes
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, pngResult.width, pngResult.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pngResult.bytes);
    // set ST-coordinate system
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
    // apply texture filter rules
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
    // mipmap
    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
  }

  @Override
  public void prepareMatrices() {
    if (vertexPositions == null) {
      throw new RuntimeException("No vertexPositions defined!");
    }

    // scale, translate, rotate model
    Matrix4f modelMatrix = new Matrix4f();
    Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
    Matrix4f.translate(modelPosition, modelMatrix, modelMatrix);
    Matrix4f.rotate((float)Math.toRadians(modelRotation.z), Z_AXIS, modelMatrix, modelMatrix);
    Matrix4f.rotate((float)Math.toRadians(modelRotation.y), Y_AXIS, modelMatrix, modelMatrix);
    Matrix4f.rotate((float)Math.toRadians(modelRotation.x), X_AXIS, modelMatrix, modelMatrix);

    // Set up view matrix (look at)
    Matrix4f viewMatrix = new Matrix4f();
    Matrix4f.translate(viewPosition, viewMatrix, viewMatrix);

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

    GL20.glUseProgram(programIDs[0]);

    projectionMatrix.store(matrix4fBuffer);
    matrix4fBuffer.flip();
    GL20.glUniformMatrix4(projectionMatrixUniformLocationHandle, false, matrix4fBuffer);

    viewMatrix.store(matrix4fBuffer);
    matrix4fBuffer.flip();
    GL20.glUniformMatrix4(viewMatrixUniformLocationHandle, false, matrix4fBuffer);

    modelMatrix.store(matrix4fBuffer);
    matrix4fBuffer.flip();
    GL20.glUniformMatrix4(modelMatrixUniformLocationHandle, false, matrix4fBuffer);

    GL20.glUseProgram(0);
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
        case Keyboard.KEY_NEXT:
          Vector3f.add(modelScale, scaleAddResolution, modelScale);
          break;
        case Keyboard.KEY_PRIOR:
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
          viewPosition = new Vector3f(0.0f, 0.0f, -1.0f);
          fieldOfView = 45.0f;
          break;
      }
    }
  }

  @Override
  public void beforeRender() {
    prepareMatrices();
  }

  @Override
  public void prepareProgram() {
    int vertexShaderHandle = ShaderCompiler.loadAndCompileShader(VERTEX_SHADER_FILE, GL20.GL_VERTEX_SHADER);
    int fragmentShaderHandle = ShaderCompiler.loadAndCompileShader(FRAGMENT_SHADER_FILE, GL20.GL_FRAGMENT_SHADER);

    List<AttribLocation> attribLocationList = new ArrayList<AttribLocation>(3);
    AttribLocation inPositionAttribLocation = new AttribLocation(0, "in_position");
    AttribLocation inColorAttribLocation = new AttribLocation(1, "in_color");
    AttribLocation inTexCoordAttribLocation = new AttribLocation(2, "in_texcoord");
    attribLocationList.add(inPositionAttribLocation);
    attribLocationList.add(inColorAttribLocation);
    attribLocationList.add(inTexCoordAttribLocation);

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
  public void prepareShaders() {

  }

  @Override
  public void render() {
    // Clear The Screen And The Depth Buffer
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

    GL20.glUseProgram(programIDs[0]);

    // bind texture
    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);

    // enable the VAO
    GL30.glBindVertexArray(vaoHandle);
    GL20.glEnableVertexAttribArray(0);
    GL20.glEnableVertexAttribArray(1);
    GL20.glEnableVertexAttribArray(2);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiHandle);
    // createTextTiles the shape:
    // 1: type of shape to createTextTiles
    // 2: start index of the vertex array
    // 3: number of values for each vertex
    GL11.glDrawElements(GL11.GL_TRIANGLES, shape.getIndices().length, GL11.GL_UNSIGNED_BYTE, 0);

    if(GL11.glGetError() != GL11.GL_NO_ERROR)
    {
      throw new RuntimeException("OpenGL error: " + GLU.gluErrorString(GL11.glGetError()));
    }

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL30.glBindVertexArray(0);
    GL20.glUseProgram(0);
  }

  @Override
  public void init() {
    try {
      Display.setTitle(CubeTextured.class.getName());
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
      // enable z-buffer check
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glDepthFunc(GL11.GL_LESS);
      // enable transparency
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

    } catch (LWJGLException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  public void cleanup() {
    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL20.glDisableVertexAttribArray(2);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GL15.glDeleteBuffers(vboHandle);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GL15.glDeleteBuffers(vboiHandle);
    GL30.glBindVertexArray(0);
    GL30.glDeleteVertexArrays(vaoHandle);
    GL11.glDeleteTextures(textureHandle);
    GL20.glDeleteProgram(programIDs[0]);
    System.out.println("Cleaned up.");
  }

  @Override
  protected void updateStats(int fps) {

  }

  public static void main(String[] args) throws LWJGLException {
    CubeTextured fun = new CubeTextured();
    fun.start();
    fun.stop();
  }
}