package yetchina.play;
import java.util.HashSet;
import java.util.Set;


public class StringDupeChars {
	public static void main(String a[]){
		String[] letters = {"a", "b", "b", "c" ,"a", "d","a"};
		Set<String> duplicateTester = new HashSet<String>();
		
		for(int i = 0; i<letters.length;i++){
			if(true == duplicateTester.add(letters[i])){
				System.out.println("true>>"+letters[i]);
				
			}else{
				System.out.println("false>>"+letters[i]);
			}
		
		}
	}
	

}
