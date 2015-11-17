uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

attribute vec4 position;
attribute vec3 colour;
attribute vec4 normal;

varying vec4 fragColour;

void main(){

    vec4 worldPosition = transformationMatrix * position;
    vec4 relativePosition = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * relativePosition;

    fragColour = vec4(colour, 0);
}