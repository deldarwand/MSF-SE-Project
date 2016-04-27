// Created by Garrett May
// 21/04/2016

#ifndef Packet_Compat_h
    #define Packet_Compat_h

    /* This class, as well as Packet.h, should be used by a JeeLink, or anything similar */

    /* Call this in setup() (RF69 compatible version)
     * id should be different, band and group should be the same
     */
    void initRF_compat(int id, int band, int group);
    /* Sends a packet, if it can send now (RF69 compatible version) */
    void sendPacket_compat(Packet packet);
    /* Sends a packet, disregarding received packets (RF69 compatible version) */
    void sendPacketNow_compat(Packet packet);
    /* Receives a packet (RF69 compatible version) */
    Packet receivePacket_compat();

#endif
