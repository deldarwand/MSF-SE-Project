// SENDING PROGRAM
// Written by Garrett May
// 21/04/2016

#include <Packet.h>
#include <JeeLib.h>

#include <dht.h>
#include <Servo.h>

#define LSPIN A2
#define DHPIN A0

dht DHT;
char buffer[10];

Servo xAxis;
Servo yAxis;

int xLower = 45;
int xUpper = 140;
int yLower = 0;
int yUpper = 140;

MilliTimer sendTimer;
MilliTimer dhtTimer;
int toggle = 0;
double humidity = 0;
double temperature = 0;

void attemptSend(); 
void handleGimbal(Packet packet);
void pollDHT(dht DHT);
int degreeChange(int degree, int lowerLimit, int upperLimit);
int yCorrection(int degree);

void setup() {
  Serial.begin(57600);
  initRF(20, RF12_433MHZ, 212);
  pinMode(LSPIN, INPUT);
  xAxis.attach(5);
  yAxis.attach(6);
  pollDHT();
  delay(1000);
}

void loop() {
  // Read any packets - looking for RotationPacket for pitch and yaw
  Packet rPacket = receivePacket();
  handleGimbal(rPacket);
  if(sendTimer.poll(53)) attemptSend();
  if(dhtTimer.poll(809)) pollDHT();
}

void attemptSend(){ 
  toggle = !toggle;
  if(toggle){
    // Send humidity
    sprintf(buffer, "%s|%d|", "H", (int) humidity);
    Packet hPacket(buffer);
    sendPacket(hPacket);
  } else{
    // Send temperature
    sprintf(buffer, "%s|%d|", "T", (int) temperature);
    Packet tPacket(buffer);
    sendPacket(tPacket);
  } 
}

void handleGimbal(Packet packet){
  if(packet.getLength() < 1) return;
  char type = 'U';
  int pitch = 0;
  int yaw = 0;
  sscanf(packet.getBuffer(), "%c|%d,%d|", &type, &pitch, &yaw);
  if(type == 'R'){
    xAxis.write(degreeChange(-yaw, xLower, xUpper));
    yAxis.write(yCorrection(-pitch));
  }
}

void pollDHT(){
   //float light = analogRead(LSPIN);
   DHT.read11(DHPIN); 
   humidity = DHT.humidity;
   temperature = DHT.temperature;
}

// GIMBAL PROGRAM
// Written by Hekla Helgadottir
// 21/04/2016

int degreeChange(int degree, int lowerLimit, int upperLimit) {
  int range = upperLimit-lowerLimit;                                 // Calculates the range of the degrees available on the gimbal
  double proportion = range / 180.00;                                // Calculates the proportion of actual range compared to projected range
  double actualDegree = (degree + 90) * proportion + lowerLimit;     // Calculates what angle to give as an instruction so that the
  return actualDegree;                                               // gimbal turns the right angle       
}

int yCorrection(int degree) {
    int actualDegree = ((degree - 90) * (-1));
    return actualDegree;                                             // Takes in an int between -90 and 90, returns a value between 0 and 180
}


