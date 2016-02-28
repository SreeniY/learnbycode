package yetchina.books.book1.cs61B.hw1;

/* OpenCommercial.java */

import java.net.*;
import java.io.*;


/**  A class that provides a main function to read five lines of a commercial
 *   Web page and print them in reverse order, given the name of a company.
 */

class OpenCommercial {

  /** Prompts the user for the name X of a company (a single string), opens
   *  the Web site corresponding to www.X.com, and prints the first five lines
   *  of the Web page in reverse order.
   *  @param arg is not used.
   *  @exception Exception thrown if there are any problems parsing the 
   *             user's input or opening the connection.
   */
  public static void main(String[] arg) throws Exception {

//    BufferedReader keyboard;
//    String inputLine;
//    keyboard = new BufferedReader(new InputStreamReader(System.in));
//
//    System.out.print("Please enter the name of a company (without spaces): ");
//    System.out.flush();        /* Make sure the line is printed immediately. */
//    inputLine = keyboard.readLine();
    //System.out.println("inputLine:"+inputLine);
	  URL url = new URL("http://news.google.com/nwshp?hl=en&tab=wn");
	  URLConnection urlConnection = url.openConnection();
	  BufferedReader htmlPage = new BufferedReader(new
	  InputStreamReader(url.openStream()));

	  String line = "";
	  StringBuffer str;
	  int c=0;
	  while((line = htmlPage.readLine()) != null) {
		  c++;
		  
	  //do something with the html line
	  System.out.println(line);
	  if(c==5)
		  break;
	  }
	  htmlPage.close();
	  urlConnection = null;
    /* Replace this comment with your solution.  */

  }
}

