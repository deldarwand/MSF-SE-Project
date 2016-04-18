/* SENDING PROGRAM */
#include <Packet.h>
#include <JeeLib.h>

#include <dht.h>

#define LSPIN A2
#define DHPIN A0

dht DHT;
char buffer[10];

MilliTimer sendTimer;
MilliTimer dhtTimer;
int toggle = 0;
double humidity = 0;
double temperature = 0;

void attemptSend(); 
void handleGimbal(Packet packet);
void pollDHT(dht DHT);

void setup() {
  Serial.begin(57600);
  initRF(20, RF12_433MHZ, 212);
  pinMode(LSPIN, INPUT);
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
  int pitch = 0.0;
  int yaw = 0.0;
  printlnPacket(packet);
  sscanf(packet.getBuffer(), "%c|%d|%d|", &type, &pitch, &yaw);
  if(type == 'R'){
    Serial.print("Pitch: ");
    Serial.println(pitch);
    Serial.print("Yaw: ");
    Serial.println(yaw);
  }  
}

void pollDHT(){
   //float light = analogRead(LSPIN);
   DHT.read11(DHPIN); 
   humidity = DHT.humidity;
   temperature = DHT.temperature;
}


