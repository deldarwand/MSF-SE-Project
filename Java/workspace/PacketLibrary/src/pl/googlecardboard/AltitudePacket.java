package pl.googlecardboard;

import pl.packet.Packet;

/**
 * A packet to store altitude.
 * Used in the MSF + Google Project.
 * @author Garrett
 *
 */
public final class AltitudePacket extends Packet{

	private float altitude;
	
	public void write(String input){
		this.altitude = new Float(input);
	}
	
	public byte[] read(){
		return ("A" + DELIMETER + altitude + DELIMETER).getBytes();
	}
	
	public float getAltitude(){
		return altitude;
	}
	
	
}
