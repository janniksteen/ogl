#version 420

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

layout(location = 0) in vec4 in_position;
layout(location = 1) in vec3 in_color;

out vec3 fragment_color;

void main() {
  gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_position;
  fragment_color = in_color; // forward to fragment shader
}