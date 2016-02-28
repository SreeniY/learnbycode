package yetchina.threads;

public class EnumeratingThreads extends Thread{
	public static void main(String args[]){
	//public void printThreads(){
		Thread t1 = new Thread();
		t1.setName("T1name");
		t1.start();
		
		Thread ta[] = new Thread[Thread.activeCount()];
		
		int n = Thread.enumerate(ta);
		for(int i=0;i<n;i++){
			System.out.println("Thread "+i+" is "+ta[i].getName());
		}
	}

}
