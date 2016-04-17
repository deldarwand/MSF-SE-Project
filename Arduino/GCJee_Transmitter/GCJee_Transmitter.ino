
/* SENDING PROGRAM */
#include <JeeLib.h>
#include <dht.h>

#define LSPIN A2
#define DHPIN A0

dht DHT;

char buffer[10];

void sendValue(char* p, int len){
  while(!rf12_canSend()){
    //Serial.println("Unable to send");
    rf12_recvDone();
  }
  Serial.println("Sending");
  Serial.println(p);
  rf12_sendStart(0, p, len);
  Serial.println("Sent");
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(57600);
  rf12_initialize(20, RF12_433MHZ, 212);
  pinMode(LSPIN, INPUT);
  delay(1000);
}

void loop() {
  // put your main code here, to run repeatedly:
  float light = analogRead(LSPIN);
  DHT.read11(DHPIN);
  double humidity = DHT.humidity;
  double temperature = DHT.temperature;
  Serial.print("Current humidity = ");
  Serial.print(humidity);
  Serial.print("%  ");
  Serial.print("temperature = ");
  Serial.print(temperature); 
  Serial.println("C  ");
  delay(800); 
  
  //Serial.println("About to send");
  //rf12_sendStart(0, "hello!", 6);
  sendValue("H", 1);
  sprintf(buffer, "%d", (int) humidity);
  sendValue(buffer, sizeof buffer);
  sendValue("T", 1);
  sprintf(buffer, "%d", (int) temperature);
  sendValue(buffer, sizeof buffer);
  Serial.println(light);
  //Serial.println("Sent: hello!");
}
