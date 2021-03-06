package pl.googlecardboard;

import pl.packet.Packet;

/**
 * A packet to store a Bitmap.
 * Used in the MSF + Google Project.
 * @author Garrett
 *
 */
public final class BitmapPacket extends Packet{

	private byte[] bitmap;
	
	public void write(byte[] input){
		this.bitmap = input;
	}
	
	public byte[] read(){
		return ("B" + DELIMETER + new String(bitmap) + DELIMETER).getBytes();
	}
	
	public byte[] getBitmapAsBytes(){
		return bitmap;
	}
}
