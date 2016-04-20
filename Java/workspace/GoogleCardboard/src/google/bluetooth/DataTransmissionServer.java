package google.bluetooth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fazecast.jSerialComm.SerialPort;

import pl.googlecardboard.AltitudePacket;
import pl.googlecardboard.BitmapPacket;
import pl.googlecardboard.GPSPacket;
import pl.googlecardboard.HumidityPacket;
import pl.googlecardboard.RotationPacket;
import pl.googlecardboard.TemperaturePacket;
import pl.packet.Packet;
import pl.packet.PacketManager;

public enum DataTransmissionServer {

	INSTANCE;
	
	private PacketManager packetManager;
	private List<Session> sessions;
	
	private DataTransmissionServer(){
		this.packetManager = new PacketManager();
		packetManager.addPacketClass('H', HumidityPacket.class);
		packetManager.addPacketClass('T', TemperaturePacket.class);
		packetManager.addPacketClass('G', GPSPacket.class);
		packetManager.addPacketClass('A', AltitudePacket.class);
		packetManager.addPacketClass('B', BitmapPacket.class);
		packetManager.addPacketClass('R', RotationPacket.class);
		this.sessions = new ArrayList<Session>();
	}
	
	public void start() throws Exception{
		System.out.println("Starting server");
		Thread connectionThread = new ConnectionThread(this);
		for(SerialPort port : SerialPort.getCommPorts()){
			System.out.println("Port: " + port.getDescriptivePortName() + " | Baud: " + port.getBaudRate());
		}
		System.out.println("Server started!");
		connectionThread.start();
		while(true){
			for(Session session : sessions){
				if(!session.isOpen()) session = null;
			}
		}
	}
	
	public void addSession(Session session){
		sessions.add(session);
	}
	
	public PacketManager getPacketManager(){
		return packetManager;
	}
	
	public static void main(String[] arg){
		try {
			DataTransmissionServer.INSTANCE.start();
		} catch(IOException exception) {
			System.err.println("Connection closed.");
		} catch(Exception exception){
			exception.printStackTrace();
		}
	}
}
