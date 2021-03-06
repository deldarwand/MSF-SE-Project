package pl.packet;

import java.io.IOException;
import java.io.InputStream;

import pl.exception.PLReadException;
import pl.exception.PLWriteException;

/**
 * An abstract class to represent a generic packet.
 * Extend this class to create your own packet.
 * It is recommended to make your packet final, so that
 * your packet is not subclassed and changed.
 * @author Garrett
 *
 */
public abstract class Packet {

	/**
	 * When nothing is sent
	 */
	public static final int NONE = -1;
	/**
	 * Equal to '|'
	 */
	public static final String DELIMETER = "|";
	/**
	 * Equal to ','
	 */
	public static final String SPLITTER = ",";
	/**
	 * This is the initial Serial.begin(9600), often found in Arduino projects
	 */
	public static final int SERIAL_BEGIN = 138;
	/**
	 * Newline '\n'
	 */
	public static final int NEWLINE = 10;
	/**
	 * Carriage return '\r'
	 */
	public static final int CARRIAGE_RETURN = 13;
			
	/**
	 * Write function to write input to a Packet
	 * @param input
	 * @throws PLWriteException
	 * @throws IOException 
	 */
	public void write(String input) throws PLWriteException, IOException{
		throw new PLWriteException("Unable to write packet: " + this);
	}
	
	/**
	 * Write function to write input to a Packet
	 * @param input
	 * @throws PLWriteException
	 * @throws IOException 
	 */
	public void write(byte[] input) throws PLWriteException, IOException{
		throw new PLWriteException("Unable to write packet: " + this);
	}	
	
	/**
	 * Read function to read a Packet into bytes
	 * @return
	 * @throws PLReadException
	 */
	public byte[] read() throws PLReadException, IOException{
		throw new PLReadException("Unable to read packet: " + this);
	}
	
	/**
	 * Reads a line of data, up until the delimeter, '|'
	 * Removes newlines, carriage returns, and serial begins
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String readLine(InputStream input) throws IOException{
		String str = "";
		for(int value = input.read(); value != (int) DELIMETER.charAt(0) && value != NONE; value = input.read()){
			switch(value){
				case SERIAL_BEGIN:
				case NEWLINE:
				case CARRIAGE_RETURN:
					continue;
				default:
					str += (char) value;
					break;
			}
		}
		return str.trim();		
	}
	
	@Override
	public String toString(){
		return getClass().getSimpleName();
	}
}
