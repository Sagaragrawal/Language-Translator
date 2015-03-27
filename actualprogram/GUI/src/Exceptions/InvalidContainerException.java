package Exceptions;
public class InvalidContainerException extends Exception {

	private static final long serialVersionUID = 1630299539261316052L;

	public InvalidContainerException() {
		
	}
	
	public String toString() {
		return "The file specified isn't a valid container.";
	}
	
	public void print() {
		System.out.println(toString());
	}
}
