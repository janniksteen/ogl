package dk.norgaardsteen.ogl.app;

import dk.norgaardsteen.ogl.Base;
import dk.norgaardsteen.ogl.font.FontDescription;
import dk.norgaardsteen.ogl.math.Trigonometric;
import dk.norgaardsteen.ogl.mesh.Cube;
import dk.norgaardsteen.ogl.mesh.Shape;
import dk.norgaardsteen.ogl.mesh.Vertex;
import dk.norgaardsteen.ogl.resource.ApplicationContext;
import dk.norgaardsteen.ogl.shader.ProgramLinker;
import dk.norgaardsteen.ogl.shader.ProgramLinkerResult;
import dk.norgaardsteen.ogl.shader.ShaderCompiler;
import dk.norgaardsteen.ogl.shader.shared.AttribLocation;
import dk.norgaardsteen.ogl.shader.shared.UniformLocation;
import dk.norgaardsteen.ogl.text.Text2D;
import dk.norgaardsteen.ogl.text.TexturedTextTile;
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
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.Buffer;
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
public class CubeWithTextDisplay extends Base {

  private static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
  private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
  private static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

  private int displayWidth = 1280;
  private int displayHeight = 720;

  private List<ProgramLinkerResult> programsLinked = new ArrayList<ProgramLinkerResult>();

  private int cubeVAOHandle = 0;
  private int cubeVBOHandle = 0;
  private int cubeVBOIndicesHandle = 0;

  private int fontTextureHandle = 0;
  private int textTilesVAOHandle = 0;
  private int textTilesVBOHandle = 0;
  private int textTilesVBOIndicesHandle = 0;

  private int textTilesIndicesCount = 0;

  private int projectionMatrixUniformLocationHandle = 0;
  private int viewMatrixUniformLocationHandle = 0;
  private int modelMatrixUniformLocationHandle = 0;
  private int fontTextureUniformLocationHandle = 0;

  private Matrix4f modelMatrix;
  private Matrix4f viewMatrix;
  private Matrix4f projectionMatrix;
  private final FloatBuffer matrix4fModelBuffer = BufferUtils.createFloatBuffer(16);
  private final FloatBuffer matrix4fViewBuffer = BufferUtils.createFloatBuffer(16);
  private final FloatBuffer matrix4fProjectionBuffer = BufferUtils.createFloatBuffer(16);

  private static final String CUBE_VERTEX_SHADER_FILE = "src/main/resources/shaders/cube.vsh";
  private static final String CUBE_FRAGMENT_SHADER_FILE = "src/main/resources/shaders/cube.fsh";
  private static final String TEXT2D_VERTEX_SHADER_FILE = "src/main/resources/shaders/text2d.vsh";
  private static final String TEXT2D_FRAGMENT_SHADER_FILE = "src/main/resources/shaders/text2d.fsh";

  private final Shape cube = new Cube();

  private Vector3f modelScale = new Vector3f(0.5f, 0.5f, 0.5f);
  private Vector3f modelPosition = new Vector3f(0.0f, 0.0f, 0.0f);
  private Vector3f modelRotation = new Vector3f(0.0f, 0.0f, 0.0f);

  private Vector3f viewPosition = new Vector3f(0.0f, 0.0f, -1.5f);
  private float viewPositionDelta = -0.05f;

  private float fieldOfView = 67.0f;
  private final float fieldOfViewDelta = 1.0f;

  private final float rotationDelta = 5f;
  private final float scaleDelta = 0.05f;
  private final float positionDelta = 0.05f;
  private final Vector3f scaleAddResolution = new Vector3f(scaleDelta, scaleDelta, scaleDelta);
  private final Vector3f scaleMinusResolution = new Vector3f(-scaleDelta, -scaleDelta, -scaleDelta);

  private final static String FONT_TEXTURE_ATLAS_FILE = "src/main/resources/img/uc0x0_0xff_Liberation Mono_x6_y12.png";
  private final static String FONT_DESCRIPTION_FILE = "src/main/resources/img/uc0x0_0xff_Liberation Mono_x6_y12.fnt";

  private FontDescription fontDescription = new FontDescription(FONT_DESCRIPTION_FILE);

  private boolean alphaBlend = true;
  private boolean showStats = true;

  private boolean subTextTileData = false;
  private boolean subTextTileIndices = false;

  private boolean wireframe = false;

  @Override
  public void prepareBuffers() {
    // cube buffers
    Collection<Vertex> vertexList = cube.getVertices();
    FloatBuffer cubeModelBuffer = BufferUtils.createFloatBuffer(vertexList.size() * Vertex.TOTAL_ELEMENT_COUNT);
    for (Vertex vertex : vertexList) {
      cubeModelBuffer.put(vertex.getXYZW());
      cubeModelBuffer.put(vertex.getRGBA());
    }
    cubeModelBuffer.flip();

    ShortBuffer cubeIndicesBuffer = BufferUtils.createShortBuffer(cube.getIndices().length);
    cubeIndicesBuffer.put(cube.getIndices());
    cubeIndicesBuffer.flip();

    cubeVBOIndicesHandle = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cubeVBOIndicesHandle);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cubeIndicesBuffer, GL15.GL_STATIC_DRAW);

    cubeVBOHandle = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cubeVBOHandle);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cubeModelBuffer, GL15.GL_STREAM_DRAW);

    // describe it:
    cubeVAOHandle = GL30.glGenVertexArrays();
    GL30.glBindVertexArray(cubeVAOHandle);
    // * 1: array index (in VAO)
    // * 2: number of values pr. vertex
    // * 3: value type
    // * 4: normalized or not
    // * 5: stride (spacing between values) (float size in bytes * total floats per vertex)
    // * 6: offset to values from the start of array (idx=0)
    int stride = Vertex.ELEMENT_BYTES * (Vertex.POSITION_ELEMENT_COUNT + Vertex.COLOR_ELEMENT_COUNT);
    int colorValuesByteOffset = Vertex.ELEMENT_BYTES * Vertex.POSITION_ELEMENT_COUNT;
    // assign positions
    GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, stride, 0);
    // assign colors
    GL20.glVertexAttribPointer(1, Vertex.COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, stride, colorValuesByteOffset);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GL30.glBindVertexArray(0);

    // text buffers
    textTilesVBOIndicesHandle = GL15.glGenBuffers();
    textTilesVBOHandle = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textTilesVBOHandle);

    textTilesVAOHandle = GL30.glGenVertexArrays();
    GL30.glBindVertexArray(textTilesVAOHandle);

    int strideTextTiles = TexturedTextTile.TOTAL_SIZE_IN_BYTES;
    int textTileTextureValueOffset = TexturedTextTile.ELEMENT_BYTES * TexturedTextTile.VERTEX_ELEMENT_COUNT;

    GL20.glVertexAttribPointer(0, TexturedTextTile.VERTEX_ELEMENT_COUNT, GL11.GL_FLOAT, false, strideTextTiles, 0);
    GL20.glVertexAttribPointer(1, TexturedTextTile.ST_ELEMENT_COUNT, GL11.GL_FLOAT, false, strideTextTiles, textTileTextureValueOffset);

    GL30.glBindVertexArray(0);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
  }

  @Override
  public void prepareTextures() {
    prepareFontTexture();
  }

  private void prepareFontTexture() {
    PNGLoader.PNGResult pngResult = PNGLoader.load(FONT_TEXTURE_ATLAS_FILE, PNGLoader.Format.RGBA);
    fontTextureHandle = GL11.glGenTextures();

    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureHandle);
    // rgb bytes aligned and one byte each
    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
    // upload bytes
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, pngResult.width, pngResult.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pngResult.bytes);
    // mipmap
    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

    fontTextureUniformLocationHandle = GL20.glGetUniformLocation(((ProgramLinkerResult)programsLinked.get(1)).programHandle, "font_texture");
  }

  @Override
  public void prepareMatrices() {
  }

  @Override
  public void beforeRender() {
    setProjectionMatrix();
  }

  private void setProjectionMatrix() {
    // Setup projection matrix
    projectionMatrix = new Matrix4f();
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

    // Set up view matrix (look at)
    viewMatrix = new Matrix4f();
    Matrix4f.translate(viewPosition, viewMatrix, viewMatrix);

    // scale, translate and rotate model
    modelMatrix = new Matrix4f();
    Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
    Matrix4f.translate(modelPosition, modelMatrix, modelMatrix);
    Matrix4f.rotate((float)Math.toRadians(modelRotation.z), Z_AXIS, modelMatrix, modelMatrix);
    Matrix4f.rotate((float)Math.toRadians(modelRotation.y), Y_AXIS, modelMatrix, modelMatrix);
    Matrix4f.rotate((float) Math.toRadians(modelRotation.x), X_AXIS, modelMatrix, modelMatrix);

    projectionMatrix.store(matrix4fProjectionBuffer);
    matrix4fProjectionBuffer.flip();

    viewMatrix.store(matrix4fViewBuffer);
    matrix4fViewBuffer.flip();

    modelMatrix.store(matrix4fModelBuffer);
    matrix4fModelBuffer.flip();
  }

  @Override
  public void input() {
    if (Display.wasResized()) {
      displayWidth = Display.getWidth();
      displayHeight = Display.getHeight();
      GL11.glViewport(0, 0, displayWidth, displayHeight);
    }

    while (Keyboard.next()) {
      // Only listen to events where the key was pressed (down event)
      if (!Keyboard.getEventKeyState()) {
        continue;
      }

      switch (Keyboard.getEventKey()) {
        // Position
        case Keyboard.KEY_UP:
          modelPosition.y += positionDelta;
          break;
        case Keyboard.KEY_DOWN:
          modelPosition.y -= positionDelta;
          break;
        case Keyboard.KEY_LEFT:
          modelPosition.x -= positionDelta;
          break;
        case Keyboard.KEY_RIGHT:
          modelPosition.x += positionDelta;
          break;
        case Keyboard.KEY_HOME:
          modelPosition.z += positionDelta;
          break;
        case Keyboard.KEY_END:
          modelPosition.z -= positionDelta;
          break;

        // Scale
        case Keyboard.KEY_NEXT:
          Vector3f.add(modelScale, scaleAddResolution, modelScale);
          break;
        case Keyboard.KEY_PRIOR:
          Vector3f.add(modelScale, scaleMinusResolution, modelScale);
          break;

        // Z rotation
        case Keyboard.KEY_Q:
          modelRotation.z += rotationDelta;
          break;
        case Keyboard.KEY_E:
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

        case Keyboard.KEY_F3:
          viewPosition.z += viewPositionDelta;
          break;

        case Keyboard.KEY_F4:
          viewPosition.z -= viewPositionDelta;
          break;

        case Keyboard.KEY_F9:
          if (wireframe) {
            wireframe = false;
          } else {
            wireframe = true;
          }
          break;

        case Keyboard.KEY_F10:
          if (alphaBlend) {
            alphaBlend = false;
          } else {
            alphaBlend = true;
          }
          break;

        case Keyboard.KEY_F12:
          if (showStats) {
            showStats = false;
          } else {
            showStats = true;
          }
          break;

        // reset
        case Keyboard.KEY_ESCAPE:
          modelScale = new Vector3f(0.5f, 0.5f, 0.5f);
          modelPosition = new Vector3f(0.0f, 0.0f, 0.0f);
          modelRotation = new Vector3f(0.0f, 0.0f, 0.0f);
          viewPosition = new Vector3f(0.0f, 0.0f, -1.5f);
          fieldOfView = 67.0f;
          prepareMatrices();
          break;
      }
    }
  }

  @Override
  public void prepareProgram() {
    int cubeVertexShaderHandle = ShaderCompiler.loadAndCompileShader(CUBE_VERTEX_SHADER_FILE, GL20.GL_VERTEX_SHADER);
    int cubeFragmentShaderHandle = ShaderCompiler.loadAndCompileShader(CUBE_FRAGMENT_SHADER_FILE, GL20.GL_FRAGMENT_SHADER);

    List<AttribLocation> attribLocationList = new ArrayList<>(3);
    AttribLocation inPositionAttribLocation = new AttribLocation(0, "in_position");
    AttribLocation inColorAttribLocation = new AttribLocation(1, "in_color");
    attribLocationList.add(inPositionAttribLocation);
    attribLocationList.add(inColorAttribLocation);

    List<UniformLocation> uniformLocationList = new ArrayList<>(3);
    UniformLocation projectionMatrixUniformLocation = new UniformLocation(UniformLocation.PROJECTION_MATRIX_UNIFORM_LOCATION_NAME);
    UniformLocation viewMatrixUniformLocation = new UniformLocation(UniformLocation.VIEW_MATRIX_UNIFORM_LOCATION_NAME);
    UniformLocation modelMatrixUniformLocation = new UniformLocation(UniformLocation.MODEL_MATRIX_UNIFORM_LOCATION_NAME);
    uniformLocationList.add(projectionMatrixUniformLocation);
    uniformLocationList.add(viewMatrixUniformLocation);
    uniformLocationList.add(modelMatrixUniformLocation);

    ProgramLinkerResult cubeProgramLinkerResult = ProgramLinker.link(new int[]{cubeVertexShaderHandle, cubeFragmentShaderHandle}, attribLocationList, uniformLocationList);
    programsLinked.add(cubeProgramLinkerResult);

    for(UniformLocation uniformLocation : cubeProgramLinkerResult.getUniformLocations()) {
      if (uniformLocation.name.equals(UniformLocation.PROJECTION_MATRIX_UNIFORM_LOCATION_NAME)) {
        projectionMatrixUniformLocationHandle = uniformLocation.handle;
      } else if (uniformLocation.name.equals(UniformLocation.VIEW_MATRIX_UNIFORM_LOCATION_NAME)) {
        viewMatrixUniformLocationHandle = uniformLocation.handle;
      } else if (uniformLocation.name.equals(UniformLocation.MODEL_MATRIX_UNIFORM_LOCATION_NAME)) {
        modelMatrixUniformLocationHandle = uniformLocation.handle;
      } else
        throw new RuntimeException("Unknown uniform location name: " + uniformLocation.name);
    }

    int textVertexShaderHandle = ShaderCompiler.loadAndCompileShader(TEXT2D_VERTEX_SHADER_FILE, GL20.GL_VERTEX_SHADER);
    int textFragmentShaderHandle = ShaderCompiler.loadAndCompileShader(TEXT2D_FRAGMENT_SHADER_FILE, GL20.GL_FRAGMENT_SHADER);

    ProgramLinkerResult textProgramLinkerResult = ProgramLinker.link(new int[]{textVertexShaderHandle, textFragmentShaderHandle}, null, null);
    programsLinked.add(textProgramLinkerResult);
  }

  @Override
  public void prepareShaders() {
  }

  @Override
  public void render() {
    // Clear The Screen And The Depth Buffer
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    // render cube

    GL20.glUseProgram(programsLinked.get(0).programHandle);
    GL20.glUniformMatrix4(projectionMatrixUniformLocationHandle, false, matrix4fProjectionBuffer);
    GL20.glUniformMatrix4(viewMatrixUniformLocationHandle, false, matrix4fViewBuffer);
    GL20.glUniformMatrix4(modelMatrixUniformLocationHandle, false, matrix4fModelBuffer);

    // set buffers
    GL30.glBindVertexArray(cubeVAOHandle);
    GL20.glEnableVertexAttribArray(0);
    GL20.glEnableVertexAttribArray(1);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cubeVBOHandle);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, cubeVBOIndicesHandle);

    GL11.glFrontFace(GL11.GL_CCW);
    GL11.glCullFace(GL11.GL_BACK);
    GL11.glEnable(GL11.GL_CULL_FACE);

    GL11.glDrawElements(GL11.GL_TRIANGLES, cube.getIndices().length, GL11.GL_UNSIGNED_SHORT, 0);

    GL11.glDisable(GL11.GL_CULL_FACE);

    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

    if (showStats) {
      GL20.glUseProgram(programsLinked.get(1).programHandle);
      GL13.glActiveTexture(GL13.GL_TEXTURE0);
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureHandle);
      GL20.glUniform1i(fontTextureUniformLocationHandle, 0);

      GL30.glBindVertexArray(textTilesVAOHandle);
      GL20.glEnableVertexAttribArray(0);
      GL20.glEnableVertexAttribArray(1);

      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textTilesVBOHandle);
      GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, textTilesVBOIndicesHandle);

      GL11.glDisable(GL11.GL_DEPTH_TEST);
      if (alphaBlend) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
      }

      GL11.glDrawElements(GL11.GL_TRIANGLES, textTilesIndicesCount, GL11.GL_UNSIGNED_SHORT, 0);

      if (alphaBlend) {
        GL11.glDisable(GL11.GL_BLEND);
      }
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL20.glDisableVertexAttribArray(0);
      GL20.glDisableVertexAttribArray(1);
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
      GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
  }

  @Override
  public void init() {
    try {
      Display.setTitle(CubeWithTextDisplay.class.getName());
      Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
      Display.setResizable(true);

      Keyboard.enableRepeatEvents(true);

      // set up OpenGL to run in forward-compatible mode
      // so that using deprecated functionality will
      // throw an error. This ensures that we are making
      // use of OpenGL 4.2
      PixelFormat pixelFormat = new PixelFormat();
      ContextAttribs contextAttributes = new ContextAttribs(4, 2);
      contextAttributes.withForwardCompatible(true);
      contextAttributes.withProfileCore(true);
      Display.create(pixelFormat, contextAttributes);

      // set viewport and blank color
      GL11.glViewport(0, 0, displayWidth, displayHeight);
      GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
      // enable z-buffer check
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glDepthFunc(GL11.GL_LESS);
      GL11.glDepthMask(true);

    } catch (LWJGLException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  public void cleanup() {
    GL11.glDeleteTextures(fontTextureHandle);

    for (ProgramLinkerResult programLinkerResult : programsLinked) {
      GL20.glUseProgram(programLinkerResult.programHandle);
      for (int i = 0 ;i < programLinkerResult.getShaderHandles().length; i++) {
        GL20.glDetachShader(programLinkerResult.programHandle, programLinkerResult.getShaderHandles()[i]);
        GL20.glDeleteShader(programLinkerResult.getShaderHandles()[i]);
      }
      GL20.glDeleteProgram(programLinkerResult.programHandle);
    }

    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GL15.glDeleteBuffers(cubeVBOHandle);
    GL15.glDeleteBuffers(textTilesVBOHandle);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GL30.glBindVertexArray(0);
    GL15.glDeleteBuffers(cubeVBOIndicesHandle);
    GL15.glDeleteBuffers(textTilesVBOIndicesHandle);
    GL30.glDeleteVertexArrays(cubeVAOHandle);
    GL30.glDeleteVertexArrays(textTilesVAOHandle);
    System.out.println("Cleaned up.");
  }

  @Override
  protected void updateStats(int fps) {
    if (showStats) {
      Collection<TexturedTextTile> texturedTextTiles = new ArrayList<>();
      Collection<TexturedTextTile> fpsTextTiles = Text2D.createTextTiles("fps: " + fps, fontDescription, 0, 0, ApplicationContext.getDisplayXSize(), ApplicationContext.getDisplayYSize(), 0);
      Collection<TexturedTextTile> rotTextTiles = Text2D.createTextTiles("rot [x:" + modelRotation.x + ",y:" + modelRotation.y + ",z:" + modelRotation.z + "]", fontDescription, 0, 14, ApplicationContext.getDisplayXSize(), ApplicationContext.getDisplayYSize(), fpsTextTiles.size());
      Collection<TexturedTextTile> fovTextTiles = Text2D.createTextTiles("fov: " + fieldOfView, fontDescription, 0, 28, ApplicationContext.getDisplayXSize(), ApplicationContext.getDisplayYSize(), fpsTextTiles.size() + rotTextTiles.size());
      Collection<TexturedTextTile> posTextTiles = Text2D.createTextTiles("pos [x:" + modelPosition.x + ",y:" + modelPosition.y + ",z:" + modelPosition.z + "]", fontDescription, 0, 42, ApplicationContext.getDisplayXSize(), ApplicationContext.getDisplayYSize(), fpsTextTiles.size() + rotTextTiles.size() + fovTextTiles.size());
      Collection<TexturedTextTile> scaleTextTiles = Text2D.createTextTiles("scale [x: " + modelScale.x + ",y:" + modelScale.y + ",z:" + modelScale.z + "]", fontDescription, 0, 56, ApplicationContext.getDisplayXSize(), ApplicationContext.getDisplayYSize(), fpsTextTiles.size() + rotTextTiles.size() + fovTextTiles.size() + posTextTiles.size());
      Collection<TexturedTextTile> vposTextTiles = Text2D.createTextTiles("vpos [x: " + viewPosition.x + ",y:" + viewPosition.y + ",z:" + viewPosition.z + "]", fontDescription, 0, 70, ApplicationContext.getDisplayXSize(), ApplicationContext.getDisplayYSize(), fpsTextTiles.size() + rotTextTiles.size() + fovTextTiles.size() + posTextTiles.size() + scaleTextTiles.size());

      texturedTextTiles.addAll(fpsTextTiles);
      texturedTextTiles.addAll(rotTextTiles);
      texturedTextTiles.addAll(fovTextTiles);
      texturedTextTiles.addAll(posTextTiles);
      texturedTextTiles.addAll(scaleTextTiles);
      texturedTextTiles.addAll(vposTextTiles);

      int newTextTilesIndicesCount = TexturedTextTile.INDICES_ELEMENT_COUNT * texturedTextTiles.size();

      if (textTilesIndicesCount != newTextTilesIndicesCount) {
        textTilesIndicesCount = newTextTilesIndicesCount;
        subTextTileData = false;
        subTextTileIndices = false;
      }

      FloatBuffer textTileBuffer = BufferUtils.createFloatBuffer(TexturedTextTile.TOTAL_ELEMENT_COUNT * TexturedTextTile.TOTAL_VERTICES_COUNT * texturedTextTiles.size());
      ShortBuffer textTileIndicesBuffer = BufferUtils.createShortBuffer(textTilesIndicesCount);

      for (TexturedTextTile texturedTextTile : texturedTextTiles) {
        for (Vertex vertex : texturedTextTile.getVertices()) {
          textTileBuffer.put(vertex.getXY());
          textTileBuffer.put(vertex.getST());
        }
        textTileIndicesBuffer.put(texturedTextTile.getIndices());
      }
      textTileBuffer.flip();
      textTileIndicesBuffer.flip();

      // optimization: force to reallocate buffers when number of tiles changes, otherwise just overwrite existing buffers (in GL driver)
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textTilesVBOHandle);
      if (subTextTileData) {
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, textTileBuffer);
      } else {
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textTileBuffer, GL15.GL_DYNAMIC_DRAW);
        subTextTileData = true;
      }

      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textTilesVBOIndicesHandle);
      if (subTextTileIndices) {
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, textTileIndicesBuffer);
      } else {
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textTileIndicesBuffer, GL15.GL_DYNAMIC_DRAW);
        subTextTileIndices = true;
      }

      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
  }

  public static void main(String[] args) throws LWJGLException {
    CubeWithTextDisplay fun = new CubeWithTextDisplay();
    fun.start();
    fun.stop();
  }
}