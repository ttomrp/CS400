package application;

public class InvalidSongException extends Exception {
	private String field;
	
	public InvalidSongException(String field) {
		this.field = field;
	}
	
	@Override
	public String toString() {
		return String.format("a Song must have %s", this.field);
	}
}
