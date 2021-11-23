package p0_Scanner;
import java.util.Scanner;

public class p0Test {
	
	public static void main(String[] args) {
	Scanner sc = new Scanner(System.in);
	System.out.println("Enter an int: ");
	String userInput = sc.nextLine();
	System.out.println("Your int = " + userInput);
	sc.close();
	}

	
}
