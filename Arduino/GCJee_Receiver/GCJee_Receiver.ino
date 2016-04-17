/* RECEIVING PROGRAM */
#include <Packet.h>
#include <Packet_Compat.h>
#include <JeeLib.h>

void setup() {
  Serial.begin(57600);
  initRF_compat(21, RF12_433MHZ, 212);
  delay(1000);
}

void loop() {
  // Recive humidity and temperature packets - send them off
  Packet packet = receivePacket_compat();
  printlnPacket(packet);
  
  // TODO: Read packet from serial
  // Read RotationPacket from serial and send
  // Packet rPacket = readPacketFromSerial();
  //Packet rPacket("R|45|46|");
  //sendPacket(rPacket);  
}
