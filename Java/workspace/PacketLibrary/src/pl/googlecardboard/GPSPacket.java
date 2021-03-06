package pl.googlecardboard;

import pl.packet.Packet;

/**
 * A packet to store GPS coordinates.
 * Used in the MSF + Google Project.
 * @author Garrett May
 *
 */
public final class GPSPacket extends Packet{

	private float latitude;
	private float longitude;
	
	public void write(String input){
		String[] l = input.split(SPLITTER);
		if(l.length == 2){
			latitude = Float.parseFloat(l[0]);
			longitude = Float.parseFloat(l[1]);
		}
	}
	
	public byte[] read(){
		return ("G" + DELIMETER + latitude + SPLITTER + longitude + DELIMETER).getBytes();
	}
	
	public float getLatitude(){
		return latitude;
	}
	
	public float getLongitude(){
		return longitude;
	}
}
