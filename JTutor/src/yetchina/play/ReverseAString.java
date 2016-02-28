package yetchina.play;

public class ReverseAString {
	public static void main(String a[]){
		String s = "HelloWorld";
		String s1 = reverse(s);
//		for(int i=s.length()-1;i>=0;i--){
//			s1+=s.charAt(i);
//			System.out.print(s.charAt(i));
//		}
//		System.out.println();
		System.out.println(s1);
	}
	public static String reverse(String s){
		if(s.length()==1)
			return s;
		else{
			//s1+=s.charAt(s.length()-1);
			//return s.charAt(s.length()-1)+ reverse(s.substring(0,s.length()-1));
			return reverse(s.substring(1))+s.charAt(0);
		}
	}

}
