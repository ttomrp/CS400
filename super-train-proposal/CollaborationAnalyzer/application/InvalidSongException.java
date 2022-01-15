package application;

/**
 * This class defines the invalid song exception
 * @author Adam Cook, Felix Lin, Jonathan McMahon, Tomas Perez, Matthias
 * Schmitz
 *
 */
@SuppressWarnings("serial")
public class InvalidSongException extends Exception {
	private String field;
	
	/**
	 * Constructor to create exception
	 */
	public InvalidSongException(String field) {
		this.field = field;
	}
	
	
	/**
	 * Return exception's method
	 * @return message describing why song was invalid
	 */
	@Override
	public String getMessage() {
		return String.format("a Song must have %s", this.field);
	}
}
