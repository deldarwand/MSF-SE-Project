
uniform mat4 projectionMatrix;


uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
//uniform mat4 u_MVP;
//uniform mat4 u_MVMatrix;
//uniform vec3 u_LightPos;

//attribute vec4 a_Position;
//attribute vec4 a_Color;
//attribute vec3 a_Normal;

attribute vec4 position;
//attribute vec2 textureCoordinates;
//attribute vec4 colour;
attribute vec3 normal;

varying vec4 v_Color;
varying vec3 v_Grid;

void main() {
   vec3 u_LightPos = vec3(0.0,0.0,0.0);
   v_Grid = vec3(transformationMatrix * position);
   vec4 a_Color = vec4(0.2, 0.5, 0.8, 1.0);

   mat4 u_MVMatrix = transformationMatrix * viewMatrix;
   vec3 modelViewVertex = vec3(u_MVMatrix * position);
   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(normal, 0.0));

   float distance = length(u_LightPos - modelViewVertex);
   vec3 lightVector = normalize(u_LightPos - modelViewVertex);
   float diffuse = max(dot(modelViewNormal, lightVector), 0.5);

   diffuse = diffuse * (1.0 / (1.0 + (0.00001 * distance * distance)));
   v_Color = a_Color * diffuse;

   vec4 worldPosition = transformationMatrix * position;
   vec4 relativePosition = viewMatrix * worldPosition;
   gl_Position = projectionMatrix * relativePosition;
}
