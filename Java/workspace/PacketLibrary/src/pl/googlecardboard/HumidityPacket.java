package pl.googlecardboard;

import pl.packet.Packet;

/**
 * A packet to store humidity.
 * Used in the MSF + Google Project.
 * @author Garrett May
 *
 */
public final class HumidityPacket extends Packet{
	
	private int humidity;
	
	public void write(String input){
		this.humidity = new Integer(input);	
	}
	
	public byte[] read(){
		return ("H" + DELIMETER + humidity + DELIMETER).getBytes();
	}
	
	public int getHumidity(){
		return humidity;
	}
}
