#version 420 core

uniform sampler2D cube_texture;

in vec4 fragment_color;
in vec2 texture_coord;

out vec4 color;

void main() {
  color = fragment_color;
  color = texture(cube_texture, texture_coord);
}