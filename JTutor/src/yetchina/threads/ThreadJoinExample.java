package yetchina.threads;

public class ThreadJoinExample {
	public static void main(String[] args){
		Thread t1=new Thread("T1");
		Thread t2 = new Thread("T2");
		try{
			System.out.println("Wait for the child threads to finish.");
			System.out.println(Thread.currentThread().getName());
			t1.join();
			if(!t1.isAlive()){
				System.out.println(Thread.currentThread().getName());
				System.out.println("Thread T1 is not alive.");
			}
				
			t2.join();
			if(!t2.isAlive()){
				System.out.println(Thread.currentThread().getName());
				System.out.println("Thread T2 is not alive.");
			}
				
		}catch(InterruptedException e){
			System.out.println("Main Thread interrupted.");
		}
		System.out.println("Exit from Main Thread.");
	}
	

}
