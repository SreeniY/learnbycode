package yetchina.play;

public class SingletonObject {
	//Private Constructor
	private SingletonObject(){
		//Nothing doing here.. 
	}
	public static SingletonObject ref;
	
	public SingletonObject getSingletonObjectRef(){
		if(ref==null){
			ref = new SingletonObject();
		}
		return ref;
	}
	
	//Override Clone method of the super class to avoid cloning of the singleton class
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
	}
}
