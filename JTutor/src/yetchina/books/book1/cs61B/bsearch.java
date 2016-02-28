package yetchina.books.book1.cs61B;

public class bsearch {
	 public static final int FAILURE = -1;

	  private static int bsearch(int[] i, int left, int right, int findMe) {
	    if (left > right) {
	      return FAILURE;                   // Base case 2:  subarray of size zero.
	    }
	    int mid = (left + right) / 2;            // Halfway between left and right.
	    if (findMe == i[mid]) {
	      return mid;                                     // Base case 1:  success!
	    } else if (findMe < i[mid]) {
	      return bsearch(i, left, mid - 1, findMe);            // Search left half.
	    } else {
	      return bsearch(i, mid + 1, right, findMe);          // Search right half.
	    }
	  }

	  public static int bsearch(int[] i, int findMe) {
	    return bsearch(i, 0, i.length - 1, findMe);
	  }


}
