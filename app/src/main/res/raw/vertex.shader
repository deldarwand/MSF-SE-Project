
in vec2 position;
out vec2 textureCoordinates;

uniform mat4 transformationMatrix;

void main() {

   screenPosition = transformationMatrix * vec4(position, 0.0, 1.0);
   textureCoordinates = vec2((position.x + 1.0) / 2.0, 1 - (position.x + 1.0) / 2.0);

}
