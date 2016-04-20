#include <Servo.h>

Servo xAxis;
Servo yAxis;

int pitch = 10;
int yaw = 10;
int xLower = 20;
int xUpper = 170;
int yLower = 10;
int yUpper = 180;

void setup() {
  yAxis.attach(5);
  xAxis.attach(6);
}

int degreeChange(int degree, int lowerLimit, int upperLimit) {
  int range = upperLimit-lowerLimit;           // Calculates the range of the degrees available on the gimbal
  double proportion = range / 180.00;          // Calculates the proportion of actual range compared to projected range
  double actualDegree = degree * proportion + lowerLimit;     // Calculates what angle to give as an instruction so that the
  return actualDegree;                                        // gimbal turns the right angle       
}

void loop() {  
  xAxis.write(degreeChange(pitch, xLower, xUpper));
  yAxis.write(degreeChange(yaw, yLower, yUpper));
  pitch = pitch + 5;
  yaw = yaw + 5;
  delay(1000);
}
