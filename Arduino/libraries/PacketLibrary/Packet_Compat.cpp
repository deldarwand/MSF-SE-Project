#define RF69_COMPAT 1

#include "Packet.h"
#include "Arduino.h"
#include "JeeLib.h"

/* FREE FUNCTIONS */

void initRF_compat(int id, int band, int group){
    rf12_initialize(id, band, group);
}

void sendPacket_compat(Packet packet){
    while(!rf12_canSend()){
        rf12_recvDone();
    }
    rf12_sendStart(0, packet.getBuffer(), packet.getLength());
}

Packet receivePacket_compat(){
    if(rf12_recvDone() && rf12_crc == 0){
        char* buffer = (char*) rf12_data;
        Packet packet(buffer, rf12_len);
        return packet;
    }
    char* buffer;
    Packet packet(buffer, 0);
    return packet;
}
