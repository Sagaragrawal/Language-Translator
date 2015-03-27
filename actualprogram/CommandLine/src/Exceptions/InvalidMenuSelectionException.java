package Exceptions;

public class InvalidMenuSelectionException extends Exception {
	private static final long serialVersionUID = -7680168297744367358L;

	public InvalidMenuSelectionException() {

	}

	public InvalidMenuSelectionException(int level) {
		System.out.println("ERROR: Invalid menu selection, " + level
				+ ", please try again.");
	}

	public String toString() {
		return "ERROR: Invalid menu selection, please try again.";
	}

	public void print() {
		System.out.println(toString());
	}
}
