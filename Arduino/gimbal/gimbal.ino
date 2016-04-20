#include <Servo.h>

Servo xAxis;
Servo yAxis;

int pitch = 10;
int yaw = 10;
int xLower = 45;
int xUpper = 140;
int yLower = 0;
int yUpper = 140;

void setup() {
  Serial.begin(9600);
  yAxis.attach(6);
  xAxis.attach(5);
}

int degreeChange(int degree, int lowerLimit, int upperLimit) {
  int range = upperLimit-lowerLimit;           // Calculates the range of the degrees available on the gimbal
  double proportion = range / 180.00;          // Calculates the proportion of actual range compared to projected range
  double actualDegree = (degree + 90) * proportion + lowerLimit;     // Calculates what angle to give as an instruction so that the
  Serial.print("degree proportion: ");
  Serial.println(actualDegree);
  return actualDegree;                                        // gimbal turns the right angle       
}

int yCorrection(int degree) {
    //Serial.println((degree - 90) * (-1));
    return ((degree - 90) * (-1));  // Takes in an int between -90 and 90, returns a value between 0 and 180
}

void loop() {  
  xAxis.write(degreeChange(yaw, xLower, xUpper));
  yAxis.write(yCorrection(pitch));
  //Serial.println(yCorrection(pitch));
  pitch = pitch + 5;
  yaw = yaw + 5;
  delay(1000);
}
