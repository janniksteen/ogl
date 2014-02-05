#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

layout(location = 0) in vec4 in_position;
layout(location = 1) in vec3 in_color;

flat out vec3 fragment_color;

void main() {
  gl_Position.xyzw = in_position;
  gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_position;

  fragment_color = in_color; // forward to fragment shader next in pipeline
}