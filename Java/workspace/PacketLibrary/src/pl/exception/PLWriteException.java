package pl.exception;

/**
 * An exception denoting a problem with writing a packet.
 * This will usually happen because this packet cannot be written.
 * If it can't be written, it's likely you're not supposed to write to it! 
 * @author Garrett May
 *
 */
public class PLWriteException extends Exception{

	private static final long serialVersionUID = -6418131594153979552L;
	
	public PLWriteException(String exception){
		super(exception);
	}
}
