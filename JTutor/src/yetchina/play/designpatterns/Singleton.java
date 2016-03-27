package yetchina.play.designpatterns;

public class Singleton {
	
	private static Singleton singletonRef = null;
	
	private Singleton(){
		//
	}
	public static Singleton getSingletonObj(){
		if(singletonRef == null){
			return new Singleton();
		}else{
			return  singletonRef;
		}
	}
	
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
