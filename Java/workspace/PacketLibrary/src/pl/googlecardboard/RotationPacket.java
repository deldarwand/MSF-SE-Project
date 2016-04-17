package pl.googlecardboard;

import pl.packet.Packet;

/**
 * A packet to store pitch and yaw.
 * Used in the MSF + Google Project.
 * @author Garrett
 *
 */
public final class RotationPacket extends Packet{

	private float pitch;
	private float yaw;
	
	public void write(String input){
		String[] r = input.split(SPLITTER);
		if(r.length == 2){
			this.pitch = Integer.parseInt(r[0]);
			this.yaw = Integer.parseInt(r[1]);
		}
	}
	
	public byte[] read(){
		return ("R" + DELIMETER + pitch + SPLITTER + yaw + DELIMETER).getBytes();
	}
	
	public float getPitch(){
		return pitch;
	}
	
	public float getYaw(){
		return yaw;
	}
}