
#ifndef Packet_h
    #define Packet_h

    class Packet{
    private:
        char* buffer;
        int length;
    public:
        Packet(char* buffer, int length);
        Packet(char* buffer);
        Packet(const Packet& object);
        ~Packet();
        char* getBuffer() const;
        int getLength() const;
    };

    void initRF(int id, int band, int group);
    void sendPacket(Packet packet);
    void sendPacketNow(Packet packet);
    Packet receivePacket();
    Packet readPacketFromSerial();
    void printPacket(Packet packet);
    void printlnPacket(Packet packet);
    int getBufferLength(char* buffer);

#endif
