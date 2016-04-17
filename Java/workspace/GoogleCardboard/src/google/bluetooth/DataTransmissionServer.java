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
		//Packet packet = packetManager.new_Packet("H|34|");
		//System.out.println("Value: " + new String(packet.read()));
		connectionThread.start();		
		/*SerialPort port = SerialPort.getCommPort("COM5");
		port.openPort();
		while(true){
			System.out.println("Value: " + Packet.readLine(port.getInputStream()));
		}*/
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