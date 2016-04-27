uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

attribute vec4 position;
attribute vec2 textureCoordinates;
attribute vec4 colour;
//attribute vec3 normal;

varying vec4 fragColour;
varying vec2 coordinates;

void main(){

    vec4 worldPosition = transformationMatrix * position;
    vec4 relativePosition = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * relativePosition;

    fragColour = colour;
    coordinates = textureCoordinates;
}