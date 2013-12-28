#version 410

uniform sampler2D font_texture;

in vec2 st;
out vec4 color;

void main() {
  color = texture(font_texture, st);
}