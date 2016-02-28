package yetchina.play.collections.old;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author syetchin
 *
 */
public class Collections1 {
	
	public boolean addAll( Collection coll )
	{
	   int sizeBefore = coll.size();
	   Iterator iter = coll.iterator();
	   while( iter.hasNext() ){
		   coll.add( iter.next() );
	   }
	   if ( sizeBefore > coll.size()){
	      return true;
	   }else{
	      return false;
	   }  
	   
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		

	}

}
