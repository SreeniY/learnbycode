package yetchina.play;

import static java.lang.System.out;
public class FizzBarr {

	/**
	 * Kevin Gillett: Write a program that prints the numbers from 1 to 100. 
 multiples of three print "Fizz" instead of the number and for the multiples of five print "Bang". For numbers which are multiples of both three and five print "FizzBang".
	 */
	public static void main(String[] args) {
		 for(int i=1; i<=100; i++){ 
	            boolean fizz = false;
	            boolean bang = false;          
	       
	            if(i%3 == 0)
	                fizz = true;
	            if(i%5 == 0)          
	                bang = true;
	                
	            if(fizz && bang)
	                System.out.println("FiZZBang");
	            else if(fizz)
	                out.println("FiZZ");
	            else if (bang)
	                out.println("Bang");
	            else
	                out.println(i);
	                
	        }
	    }

	}
