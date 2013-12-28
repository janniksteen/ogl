#version 410

uniform int displayWidth = 1280;
uniform int displayHeight = 720;

layout(location = 0) in vec2 in_position; // clip-space position
layout(location = 1) in vec2 in_st;

out vec2 st;

void main() {
  int w = displayWidth / 2;
  int h = displayHeight / 2;
  vec2 homogeneousPosition = in_position - vec2(w, h);
  homogeneousPosition /= vec2(w, h);
  gl_Position.xyzw = vec4(homogeneousPosition, 0, 1);
  st = in_st; // forward to fragment shader
}