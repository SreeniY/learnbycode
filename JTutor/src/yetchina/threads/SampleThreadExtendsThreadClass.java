package yetchina.threads;

class XThread extends Thread{
	XThread(){
	}
	XThread(String threadName){
		super(threadName);
		System.out.println(this);
		start();
	}
	public void run(){
		System.out.println(Thread.currentThread().getName());
	}
}


public class SampleThreadExtendsThreadClass {
	public static void main(String a[]){
		Thread thread1 = new Thread(new XThread(),"thread1");
		Thread thread2 = new Thread(new XThread(),"thread2");
		Thread thread3 = new XThread();
		Thread thread4 = new XThread();
		Thread thread5 = new XThread("thread5");
		Thread thread6 = new XThread();
		//Start the threads
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		thread6.start();
		try{
			Thread.currentThread().sleep(1000);
		}catch(InterruptedException e){
			
		}
		System.out.println(Thread.currentThread());
	}

}
