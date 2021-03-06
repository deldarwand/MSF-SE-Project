package pl.exception;

/**
 * An exception denoting a problem with reading a packet.
 * This will usually happen because this packet cannot be read.
 * If it can't be read, it's likely you're not supposed to read from it! 
 * @author Garrett May
 *
 */
public class PLReadException extends Exception{

	private static final long serialVersionUID = -1817790230683088040L;

	public PLReadException(String exception){
		super(exception);
	}
}
