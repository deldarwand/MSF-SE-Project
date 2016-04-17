/* SENDING PROGRAM */
#include <Packet.h>
#include <JeeLib.h>

#include <dht.h>

#define LSPIN A2
#define DHPIN A0

dht DHT;
char buffer[10];

MilliTimer sendTimer;
int toggle = 0;

void attemptSend(); 

void setup() {
  Serial.begin(57600);
  initRF(20, RF12_433MHZ, 212);
  pinMode(LSPIN, INPUT);
  delay(1000);
}

void loop() {
  if(sendTimer.poll(800)) attemptSend();
  
  // Read any packets - looking for RotationPacket for pitch and yaw
  Packet rPacket = receivePacket();
  printlnPacket(rPacket);
}

void attemptSend(){
  float light = analogRead(LSPIN);
  DHT.read11(DHPIN);  
  
  toggle = !toggle;
  if(toggle){
    // Send humidity
    double humidity = DHT.humidity;
    sprintf(buffer, "%s%d", "H|", (int) humidity);
    Packet hPacket(buffer);
    sendPacket(hPacket);
  } else{
    // Send temperature
    double temperature = DHT.temperature;
    sprintf(buffer, "%s%d", "T|", (int) temperature);
    Packet tPacket(buffer);
    sendPacket(tPacket);
  } 
}


