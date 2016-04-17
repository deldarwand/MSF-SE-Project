/* RECEIVING PROGRAM */
#include <Packet.h>
#include <Packet_Compat.h>
#include <JeeLib.h>

MilliTimer sendTimer;
int needToSend = 0;

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
  Packet tPacket = receivePacket_compat();
  printlnPacket(tPacket);
  if(sendTimer.poll(1000)) attemptSend();  
}

void attemptSend(){
  // TODO: Read packet from serial
  // Read RotationPacket from serial and send
  // Packet rPacket = readPacketFromSerial();
  Packet rPacket("R|45|46|");
  sendPacket_compat(rPacket);     
}
