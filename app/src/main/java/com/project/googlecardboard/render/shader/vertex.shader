
in vec2 position;

out vec2 textureCoordinates;

void main(){

    textureCoordinates = vec2((position.x + 1.0)/2.0, 1-(position.y+1.0)/2.0);

}