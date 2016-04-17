#define RF69_COMPAT 0

#include "Packet.h"
#include "Arduino.h"
#include "JeeLib.h"

/* PACKET FUNCTIONS */

Packet::Packet(char* buffer, int length) :
    buffer(buffer),
    length(length){
}

Packet::Packet(char* buffer) :
    buffer(buffer),
    length(getBufferLength(buffer)){
}

Packet::Packet(const Packet& object) :
    buffer(object.getBuffer()),
    length(object.getLength()){
}

Packet::~Packet(){

}

char* Packet::getBuffer() const{
    return buffer;
}

int Packet::getLength() const{
    return length;
}

/* FREE FUNCTIONS */

void initRF(int id, int band, int group){
    rf12_initialize(id, band, group);
}

void sendPacket(Packet packet){
    while(!rf12_canSend()){
        rf12_recvDone();
    }
    rf12_sendStart(0, packet.getBuffer(), packet.getLength());
}

Packet receivePacket(){
    if(rf12_recvDone() && rf12_crc == 0){
        char* buffer = (char*) rf12_data;
        Packet packet(buffer, rf12_len);
        return packet;
    }
    char* buffer;
    Packet packet(buffer, 0);
    return packet;
}

Packet readPacketFromSerial(){
    int serialByte = 0;
    char buffer[1024];
    int counter = 0;
    while(Serial.available() > 0){
        serialByte = Serial.read();
        buffer[counter++] = (char) serialByte;
    }
    Packet packet(buffer);
    return packet;
}

void printPacket(Packet packet){
    if(packet.getLength() != 0){
        char* buf = packet.getBuffer();
        for(int i = 0; i < packet.getLength(); i++){
            Serial.print(buf[i]);
        }
    }
}

void printlnPacket(Packet packet){
    if(packet.getLength() != 0){
        printPacket(packet);
        Serial.print("\n");
    }
}

int getBufferLength(char* buffer){
    int length;
    for(length = 0; buffer[length] != '\0'; length++);
    return length;
}
