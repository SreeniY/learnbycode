package yetchina.junit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class test {
	
	
//  public static void main( String[] args ) {
////	  int i = 0;
////	  int[] nums = { 4, 3, 2, 7, 1, 9 };
////	  while (i < (nums.length - 1)) {
////	    nums[i] = nums[i + 1];
////	    i++;
////	  }
////	  for(i=0;i<nums.length;i++){
////		  System.out.println(nums[i]);
////	  }
////	  
//  }
//   
//
	 public static void main(String args[]) {
//		 int i=11;
//		 if(i%2!=0&&i%3!=0)
//			 System.out.println("Prime");
//		 else 
//			 System.out.println("Not Prime");
//		 int num = 11;
//		    int i;
//		    for (i=2; i < num ;i++ ){
//		      int n = num%i;
//		      if (n==0){
//		        System.out.println("not Prime!");
//		        break;
//		      }
//		    }
//		    if(i == num){
//		      System.out.println("Prime number!");
//		    }
//		  
		 List li = new ArrayList();
		 li=getPrime(5);
		 Iterator i= li.iterator();
		 while(i.hasNext()){
			 System.out.println(i.next());
		 }
	        //System.out.println( reverseString("Hello World!"));
	    }
//	    static public String reverseString(String s){
//	        if (s.length() <= 1)
//	            return s;
//	        else {
//	            char c = s.charAt(0);
//	            return reverseString(s.substring(1))+c;
//	        }
//	    }
	   public static List getPrime(int n){
		   List l = new ArrayList();
		   int c=0;
		   int num=1;
		   while(c<=n){
			   num++;
			   if(prime(num)){
				   c++;
				   l.add(num);
			   }
		   }
		return l;   
	   }
	   public static boolean prime(int num){
		   boolean b = false;
//		   if(num%2!=0&num%3!=0&num%5!=0&num%7!=0&num%11!=0&num%71!=0&num%79!=0)
//			   b=true;
		   int i;
		    for (i=2; i < num ;i++ ){
		      int n = num%i;
		      if (n==0){
		        b=false;
		    	  //System.out.println("not Prime!");
		        break;
		      }
		      //if(i == num){
		      if(n!=0){
//			      System.out.println("Prime number!");
		        b=true;
		    }
		    }
		   return b;
	   }
}
	


