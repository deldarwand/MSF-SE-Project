/* SENDING PROGRAM */
#include <Packet.h>
#include <JeeLib.h>

#include <dht.h>

#define LSPIN A2
#define DHPIN A0

dht DHT;

char buffer[10];

void setup() {
  Serial.begin(57600);
  initRF(20, RF12_433MHZ, 212);
  pinMode(LSPIN, INPUT);
  delay(1000);
}

void loop() {
  float light = analogRead(LSPIN);
  DHT.read11(DHPIN);  
  double humidity = DHT.humidity;
  double temperature = DHT.temperature;
  delay(800);
  
  // Send humidity
  sprintf(buffer, "%s%d", "H|", (int) humidity);
  Packet hPacket(buffer);
  sendPacket(hPacket);
  
  // Send temperature
  sprintf(buffer, "%s%d", "T|", (int) temperature);
  Packet tPacket(buffer);
  sendPacket(tPacket);
  
  // Read any packets - looking for RotationPacket for pitch and yaw
  //Packet rPacket = receivePacket();
  //printlnPacket(rPacket);
}


