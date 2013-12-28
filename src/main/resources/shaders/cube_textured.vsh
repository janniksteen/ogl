#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

layout(location = 0) in vec4 in_position;
layout(location = 1) in vec3 in_color;
layout(location = 2) in vec2 in_texcoord;

out vec3 fragment_color;
out vec2 texture_coord;

void main() {
  gl_Position.xyzw = in_position;
  gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_position;

  fragment_color = in_color; // forward to fragment shader
  texture_coord = in_texcoord; // forward to fragment shader
}