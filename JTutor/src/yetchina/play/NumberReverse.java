package yetchina.play;

public class NumberReverse {
	
	 public int reverseNumber(int number){
         
	        int reverse = 0;
	        while(number != 0){
	        	System.out.println("fraction: "+number);
	            reverse = (reverse*10)+(number%10);
	            number = number/10;
	            System.out.println("Stack content: "+reverse);
	        }
	       
	        return reverse;
	    }
	     
	    public static void main(String a[]){
	        NumberReverse nr = new NumberReverse();
	        System.out.println("Result: "+nr.reverseNumber(17868));
	    }
}
