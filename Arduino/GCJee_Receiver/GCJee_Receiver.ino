/* RECEIVING PROGRAM */
#include <Packet.h>
#include <Packet_Compat.h>
#include <JeeLib.h>

MilliTimer sendTimer;

void attemptSend(); 

void setup() {
  Serial.begin(57600);
  initRF_compat(21, RF12_433MHZ, 212);
  delay(1000);
}

void loop() {
  // Receive humidity and temperature packets - send them off
  Packet hPacket = receivePacket_compat();
  printlnPacket(hPacket);
  if(sendTimer.poll(50)) attemptSend();
}

void attemptSend(){
  // TODO: Read packet from serial
  // Read RotationPacket from serial and send
  Packet rPacket = readPacketFromSerial();
  //Packet rPacket("R|45|360|");
  sendPacket_compat(rPacket);     
}
