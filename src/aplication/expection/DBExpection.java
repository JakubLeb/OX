package aplication.expection;

public class DBExpection extends RuntimeException{
	public DBExpection(String message) {
		super(message);
	}
	public DBExpection(String message, Throwable cause) {
		super(message,cause);
	}
}
