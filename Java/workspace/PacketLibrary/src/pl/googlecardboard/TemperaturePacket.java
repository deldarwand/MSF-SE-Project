package pl.googlecardboard;

import pl.packet.Packet;

/**
 * A packet to store temperature.
 * Used in the MSF + Google Project.
 * @author Garrett May
 *
 */
public final class TemperaturePacket extends Packet{

	private int temperature;
	
	public void write(String input){
		this.temperature = Integer.parseInt(input);
	}
	
	public byte[] read(){
		return ("T" + DELIMETER + temperature + DELIMETER).getBytes();
	}
	
	public int getTemperature(){
		return temperature;
	}
}
