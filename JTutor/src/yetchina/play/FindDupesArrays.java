package yetchina.play;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import org.apache.commons.lang3.ArrayUtils;

public class FindDupesArrays {
	//PayPal interview question: Check for duplicates in two arrays
	//======================================================

	public Boolean findDupeFlag(Integer[] arr1, Integer[] arr2) {

		Integer[] mergedArr = mergeArrays(arr1, arr2);

		for (int i = 0; i < mergedArr.length; i++) {
			if (findNumberInArr(mergedArr, mergedArr[i], i))
				return true;
		}
		return false;
	}

	public boolean findNumberInArr(Integer[] myArr, Integer myInt, int pos) {
		for (int i = 0; i < myArr.length; i++) {
			if (i != pos)
				if (myArr[i].equals(myInt))
					return true;
		}
		return false;
	}

	Integer[] mergeArrays(Integer[] first, Integer[] second) {
		List<Integer> both = new ArrayList<Integer>();

		both.addAll(Arrays.asList(first));
		both.addAll(Arrays.asList(second));

		return both.toArray(new Integer[both.size()]);
	}
	
	public static void main(String a[]) {

		Integer[] Arr1 = { 1, 2, 3, 3, 5, 4, 3, 2 };
		Integer[] Arr2 = { 1, 2, 8, 7, 2, 5 };

		FindDupesArrays nr = new FindDupesArrays();
		System.out.println("Result: " + nr.findDupeFlag(Arr1, Arr2));
	}
}
