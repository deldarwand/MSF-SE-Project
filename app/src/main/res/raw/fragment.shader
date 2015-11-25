precision mediump float;

uniform sampler2D texture;

varying vec4 fragColour;
varying vec2 coordinates;

void main(){

    gl_FragColor = texture2D(texture, coordinates);

}