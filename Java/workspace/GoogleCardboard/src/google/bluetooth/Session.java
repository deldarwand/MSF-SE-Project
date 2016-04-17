package google.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

import com.fazecast.jSerialComm.SerialPort;

import pl.packet.Packet;
import pl.packet.PacketManager;
import pl.packet.UnknownPacket;

public final class Session implements Runnable{

	private final InputStream inputA;
	private final InputStream inputB;
	private final OutputStream outputA;
	private final OutputStream outputB;
	private final PacketManager packetManager;
	private boolean isOpen;
	
	public Session(SerialPort port, StreamConnection connection, PacketManager packetManager) throws IOException{
		this.inputA = port.getInputStream();
		this.inputB = connection.openInputStream();
		this.outputA = port.getOutputStream();
		this.outputB = connection.openOutputStream();
		this.packetManager = packetManager;
		this.isOpen = true;
	}
	
	@Override
	public void finalize() throws IOException{
		inputA.close();
		inputB.close();
		outputA.close();
		outputB.close();
		this.isOpen = false;
	}
	
	public boolean isOpen(){
		return isOpen;
	}
	
	public void run(){
		try {
			while(true){
				process();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	private void process() throws Exception{
		System.out.println("Session: Sending packet!");
		readThenWrite(inputA, outputB);
		//readThenWrite(connection.openInputStream(), port.getOutputStream());
	}
	
	private void readThenWrite(InputStream input, OutputStream output) throws Exception{
		Packet packet = read(input);
		System.out.println("PacketType: "+ packet);
		if(!(packet instanceof UnknownPacket)){
			System.out.println("Packet: " + new String(packet.read()));
			write(output, packet);
		}
	}
	
	public void write(OutputStream output, Packet packet) throws Exception{
		output.write(packet.read());
		output.flush();
		//System.err.println("Connection closed");
		//isOpen = false;		
	}
	
	public Packet read(InputStream input) throws Exception{
		if(input.available() > 0){
			return packetManager.new_Packet(input);
		} else{
			return packetManager.new_Packet(0);
		}		
		//System.err.println("Connection closed");
		//isOpen = false;
	}	
}
