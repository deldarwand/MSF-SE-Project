package pl.googlecardboard;

import pl.packet.Packet;

/**
 * A packet to store pitch and yaw.
 * Used in the MSF + Google Project.
 * @author Garrett
 *
 */
public final class RotationPacket extends Packet{

	private int pitch;
	private int yaw;
	
	public void write(String input){
		String[] r = input.split(SPLITTER);
		if(r.length == 2){
			this.pitch = new Integer(r[0]);
			this.yaw = new Integer(r[1]);
		}
	}
	
	public byte[] read(){
		return ("R" + DELIMETER + pitch + SPLITTER + yaw + DELIMETER).getBytes();
	}
	
	public int getPitch(){
		return pitch;
	}
	
	public int getYaw(){
		return yaw;
	}
}
