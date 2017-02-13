package br.inpe.cap.asniffer.exceptions;

/**
 * Exception thrown in case of bad file format detection.
 */
@SuppressWarnings("serial")
public class FileFormatException extends Exception {
	private long position;

	public FileFormatException(String message, long position) {
		super(message);
		this.position = position;
	}	
		
	public FileFormatException(String message, Throwable cause, long position) {
		super(message, cause);
		this.position = position;
	}		
	
	/**
	 * Gets the file pointer position before which the exception has occurred.
	 */
	public long getPosition() {
		return position;
	}
}
