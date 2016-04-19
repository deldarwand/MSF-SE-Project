/* Sweep
 by BARRAGAN <http://barraganstudio.com>
 This example code is in the public domain.

 modified 8 Nov 2013
 by Scott Fitzgerald
 http://www.arduino.cc/en/Tutorial/Sweep
*/

#include <Servo.h>

Servo xAxis;  // create servo object to control a servo
Servo yAxis;

int pos = 70;    // variable to store the servo position
int sign = 0;
int change = 0;

void setup() {
  xAxis.attach(9);  // attaches the servo on pin 9 to the servo object
  yAxis.attach(10);
}

void loop() {
    sign = random(-1, 1);
    change = random(1, 5);
    pos = pos + sign*change;
    if (pos >= 0 && pos <= 180) {
      xAxis.write(pos);              // tell servo to go to position in variable 'pos'
      delay(30);                       // waits 15ms for the servo to reach the position
    }
    Serial.println(sign);
}
