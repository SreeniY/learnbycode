package yetchina.play;

public class Swap2IntsWithoutTemp {
  public static void main(String a[]){
	  int i = 2;
	  int j = 3;
	  System.out.println("i= "+i+" j= "+j);
	  i = i ^ j;
	  j = j ^ i;
	  i = i ^ j;
	  System.out.println("i= "+i+" j= "+j);
  }
}
