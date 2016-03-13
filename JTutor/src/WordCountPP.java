
public class WordCountPP {

	
	/)Write a program to read a word from a file and count the duplicate word
	Sort by most repeated
	Input: Paypal
	A:2,L:1,
	)/

	public Map<String, Integer> wordCount(Sring lineOfWords){

	//List<String> wordsInLine = new ArrayList<String>();
	  Map<String, Integer> returnMap = new HashMap<String, Integer>();
	 String givenLine  = lineOfWords;
	 while(givenLine == null){
	     String fetchedWord = givenLine.subStrig(1, ''); //get the first wrod with a space as end char.
	     returnMap.add(ftchedWord, returnMap.get((ftchedWord)+1L));
	     if(returnMap.cntains(fetchedWord ){
	         returnMap.add(ftchedWord, returnMap.get((ftchedWord)+1L));
	     }else{
	         returnMap.add(ftchedWord,1L));
	     }
	     givenLine =  givenLine.subString(''); //Assuming this give the rest of the words line after the first space
	     
	 }
	 
	 Map<Integer, String> contsWords = new HashMap<>();
	 
	 for(String word:returnMap.keys()){
	     contsWords.add(returnMap.get(word), word);
	 }
	 
	 contrsWords.sort(keys);
	 
	 return contrsWords;

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
