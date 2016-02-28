package yetchina.play;

/**
 * 
 * @author Sreeni
 * Algorithm:
 * 1: Sort the given array
 * 2: Compare the neighbors and update the sorter array
 * 3: Print array.
 *
 */
public class KijijiTest {
	/**
	 * Method: removeDuplicates method removes the duplicate values in the given array  of integers
	 * @param values: Input array of integers
	 * @return: An array of integers without duplicates
	 */
    public static int[] removeDuplicates(int[] values)
    {
      // your solution :)
    	if(values.length == 0) return values; //Sanity Check
    	int[] localNumbers = new int[values.length];
    	values = sortToArray(values);
    	int j = 0;
    	for(int i=0; i<values.length-1; i++){
    		if(values[i] != values[i+1]){
    			localNumbers[j] = values[i];
    			j++;
    		}
    	}
    	int[] localNumbers2 = new int[j];
    	for(int k = 0;k<j; k++){
    		localNumbers2[k] = localNumbers[k];
    	}
    	
    	return localNumbers2;
    }
    
    /**
     * Method: sortToArray this method sorts the given array of integer in the ascending order.
     * @param num: Input array of integers
     * @return: An array of sorted integers
     */
    public static int[] sortToArray( int[] num ){
        if(num.length == 0) return num; 
    	int j;
         boolean flag = true; 
         int temp;

         while ( flag ){
                flag= false;
                for( j=0;  j < num.length -1;  j++ ){
                       if ( num[ j ] > num[j+1] ){
                               temp = num[ j ]; 
                               num[ j ] = num[ j+1 ];
                               num[ j+1 ] = temp;
                              flag = true;
                      }
                }
          }
         return num;
    } 
    
    /**
     * Method: printArray this method prints the contents of an array of integesr to the console
     * @param values
     */
    public static void printArray(int[] values){
    	for(int i=0;i< values.length; i++){
    		System.out.print(values[i]);
    		if (i<values.length-1) System.out.print(", ");
    	}
    }
    
    /**
     * Main method to demo the solution
     * @param args
     */
	public static void main(String[] args) {
		int[] numbers = { 12,3,1, 2, 3, 3, 3, 4, 39, 10, 13, 15, 15, 17 };
		System.out.print("Original: ");
		printArray(numbers);
		
		System.out.print("\nNoDupes: ");
		printArray(removeDuplicates(numbers));		
	}
}
