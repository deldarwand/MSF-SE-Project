/* RECEIVING PROGRAM */
#define RF69_COMPAT 1

#include <JeeLib.h>
#include <avr/sleep.h>

#define LED 9

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(LED, OUTPUT);
  digitalWrite(LED, HIGH); 
  rf12_initialize(21, RF12_433MHZ, 212);
  delay(1000);
}

void loop() {
  // put your main code here, to run repeatedly:
  //Serial.println("Hello World|");
  if(rf12_recvDone() && rf12_crc == 0){
    char* buffer = (char*) rf12_data;
    //Serial.print("Received: ");
    for(int i = 0; i < rf12_len; i++){
      Serial.print(buffer[i]); 
    }
    Serial.println("|");
  }
}
