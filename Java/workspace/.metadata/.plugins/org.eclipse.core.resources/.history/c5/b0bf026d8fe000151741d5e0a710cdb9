package google.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

public class ProcessThread extends Thread{

	private final StreamConnection connection;
	private static final int NONE = -1;			// When nothing is sent
	private static final int DELIMETER = 124;	// Equivalent to '|'
	
	private int humidity;
	private int temperature;
	
	public ProcessThread(StreamConnection connection){
		this.connection = connection;
	}
	
	@Override
	public void run(){
		try{
			process();
		} catch(IOException exception){
			System.err.println("An IO exception occurred");
			exception.printStackTrace();
		}
	}
	
	private void process() throws IOException{
		InputStream input = connection.openInputStream();
		System.out.println("Waiting for input...");
		String str;
		// Check, so that it doesn't just spam println
		while(true){
			if((str = readString(input)) != ""){
				System.out.println("Value: " + str);
				OutputStream output = connection.openOutputStream();
				output.write("Have a nice day!".getBytes());
			}
		}				
	}
	
	private String readString(InputStream input) throws IOException{
		String str = "";
		int value;
		while((value = input.read()) != NONE && value != DELIMETER){
			/*
			 * There could be a better way to do this,
			 * but currently, not in a way such that
			 * it can take in any length of bytes
			 */
			str += (char) value;
		}
		return str;
	}
	
	public void setHumidity(int humidity){
		this.humidity = humidity;
	}
	
	public void setTemperature(int temperature){
		this.temperature = temperature;
	}
	
	
}
