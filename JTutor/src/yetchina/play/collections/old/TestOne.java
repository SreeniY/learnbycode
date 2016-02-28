package yetchina.play.collections.old;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class TestOne {
	public static void main(String[] args) {
//		System.out.println("Hi......");
//		int x = 1;
//		int y = x++ - ++x;
//		int z = ++y + y++;
//		boolean b = (z & 1) == 1;
//		System.out.println(x+" "+y+" "+z);
		
		Set set = new HashSet();
		    set.add("Bernadine");
		    set.add("Elizabeth");
		    set.add("Gene");
		    set.add("Elizabeth");
		    set.add("Clara");
		    System.out.println(set);
		    Set sortedSet = new TreeSet(set);
		    System.out.println(sortedSet);
	}
}


abstract class Abs    
{    
    abstract public  void method1();    
  
    public final void method2() { 
    	System.out.println("  ");
    }     
}    
