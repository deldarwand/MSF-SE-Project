
#ifndef Packet_Compat_h
    #define Packet_Compat_h

    void initRF_compat(int id, int band, int group);
    void sendPacket_compat(Packet packet);
    void sendPacketNow_compat(Packet packet);
    Packet receivePacket_compat();

#endif
