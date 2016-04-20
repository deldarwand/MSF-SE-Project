// Created by Garrett May
// 21/04/2016

#ifndef Packet_h
    #define Packet_h

    /* This class should be used by a JeeNode, or anything similar */

    /* Class representing a Packet */
    class Packet{
    private:
        char* buffer;   // Packet buffer of bytes/chars
        int length;     // Length of the buffer
    public:
        Packet(char* buffer, int length);
        Packet(char* buffer);
        Packet(const Packet& object);
        ~Packet();
        char* getBuffer() const;
        int getLength() const;
    };

    /* Call this in setup()
     * id should be different, band and group should be the same
     */
    void initRF(int id, int band, int group);
    /* Sends a packet, if it can send now */
    void sendPacket(Packet packet);
    /* Sends a packet, disregarding received packets */
    void sendPacketNow(Packet packet);
    /* Receives a packet */
    Packet receivePacket();
    /* Reads a packet from the Serial */
    Packet readPacketFromSerial();
    /* Prints a packet, if length != 0 */
    void printPacket(Packet packet);
    /* Prints a packet on a new line, if length != 0 */
    void printlnPacket(Packet packet);
    /* Finds the length of a buffer (used in constructor) */
    int getBufferLength(char* buffer);

#endif
