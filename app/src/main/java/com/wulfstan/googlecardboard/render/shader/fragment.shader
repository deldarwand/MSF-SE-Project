
in vec2 textureCoordinates;

out vec4 colour;

uniform sampler2D texture;

void main(){

    colour = texture(texture, textureCoordinates);

}