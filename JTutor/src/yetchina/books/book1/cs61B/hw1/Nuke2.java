package yetchina.books.book1.cs61B.hw1;
import java.io.*;

public class Nuke2 {
	 public static void main(String[] arg) throws Exception {

	    BufferedReader keyboard;
	    String inputLine;
	    keyboard = new BufferedReader(new InputStreamReader(System.in));
	
	    System.out.print("Please enter the string: ");
	    System.out.flush();        /* Make sure the line is printed immediately. */
	    inputLine = keyboard.readLine();
	    inputLine=inputLine.charAt(0)+inputLine.substring(2);
	    System.out.println("inputLine:"+inputLine);
	 }
}
