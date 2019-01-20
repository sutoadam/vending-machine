package hu.sutoadam.vendingmachine.exceptions;

public class MasterKeyWrongException extends Exception{
	public MasterKeyWrongException() {
		super();
	}
	
	public MasterKeyWrongException(String message) {
		super(message);
	}
}
