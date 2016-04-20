package pl.packet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A basic packet manager for Packets.
 * @author Garrett May
 *
 */
public class PacketManager {

	private final Map<Integer, Class<? extends Packet>> packets;
	
	public PacketManager(){
		this.packets = new HashMap<Integer, Class<? extends Packet>>();
		addPacketClass(0, UnknownPacket.class);
	}
	
	/**
	 * Adds a type of packet to the manager
	 * @param id The ID of the Packet. Cannot be 0, as this is reserved for the UnknownPacket
	 * @param clazz The class of the Packet
	 */
	public final void addPacketClass(int id, Class<? extends Packet> clazz){
		if(id != 0){
			packets.put(id, clazz);
		}		
	}
	
	/**
	 * Adds a type of packet to the manager. Used in the MSF + Google Project
	 * @param id The ID of the Packet, as a char. Cannot equal 0, as this is reserved for the UnknownPacket
	 * @param clazz
	 */
	public final void addPacketClass(char id, Class<? extends Packet> clazz){
		addPacketClass((int) id, clazz);
	}
	
	/**
	 * Creates a new Packet, without information, given an ID
	 * @param id The ID of the Packet
	 * @return
	 * @throws Exception
	 */
	public final Packet new_Packet(int id) throws Exception{
		Class<? extends Packet> clazz = packets.get(id);
		if(clazz == null) clazz = UnknownPacket.class; // Android not at Java 8 yet
		return clazz.newInstance();
	}
	
	/**
	 * Creates a new Packet, with information. Used in the MSF + Google Project
	 * @param input A String in the form: [char denoting packet]|[value for packet]|
	 * @return
	 * @throws Exception
	 */
	public Packet new_Packet(String str) throws Exception{
		InputStream input = new ByteArrayInputStream(str.getBytes());
		return new_Packet(input);
	}
	
	/**
	 * Creates a new Packet, with information. Used in the MSF + Google Project
	 * @param input An InputStream in the form: [char denoting packet]|[value for packet]|
	 * @return
	 * @throws Exception
	 */
	public Packet new_Packet(InputStream input) throws Exception{
		String line = Packet.readLine(input);
		if(line.length() == 0) return new UnknownPacket();
		int id = (int) line.charAt(0);
		Packet packet = new_Packet(id);
		if(!(packet instanceof UnknownPacket)) packet.write(Packet.readLine(input));
		return packet;
	}
}
