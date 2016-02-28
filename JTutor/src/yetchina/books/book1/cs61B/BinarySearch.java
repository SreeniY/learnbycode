package yetchina.books.book1.cs61B;
import yetchina.books.book1.cs61B.bsearch;

public class BinarySearch {
	public static void main(String...strings){
		int[] arr={-3,-2,-1,0,0,1,4,5};
		bsearch b= new bsearch();
		int r = b.bsearch(arr, -1);
		System.out.println("arr["+r+"]="+arr[r]);
	}

}
