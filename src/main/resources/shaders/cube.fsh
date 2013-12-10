#version 330 core

uniform sampler2D texture_diffuse;

in vec4 fragment_color;
in vec2 texture_coord;

out vec4 color;

void main() {
  color = fragment_color;
  color = texture(texture_diffuse, texture_coord);
}