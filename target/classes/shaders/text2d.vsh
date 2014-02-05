#version 420

layout(location = 0) in vec2 in_position;
layout(location = 1) in vec2 in_st;

out vec2 st;

void main() {
  gl_Position.xyzw = vec4(in_position, 0, 1);
  st = in_st; // forward to fragment shader
}