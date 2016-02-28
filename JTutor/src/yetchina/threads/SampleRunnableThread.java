package yetchina.threads;

class RunnableThread implements Runnable{
	Thread runner;
	public RunnableThread(){
	}
	public RunnableThread(String threadName){
		runner = new Thread(this, threadName);
		System.out.println(runner.getName());
		runner.start();
	}
	public void run(){
		try {
			for(int i=1;i<=5;i++){
				if(i==3)
					Thread.currentThread().sleep(500);
				System.out.println(Thread.currentThread()+" i="+i);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread());
	}
}


public class SampleRunnableThread {
	public static void main(String a[]){
		Thread thread1 = new Thread(new RunnableThread(),"thread1");
		Thread thread2 = new Thread(new RunnableThread(),"thread2");
		RunnableThread thread3 = new RunnableThread("thread3");
		thread1.start();
		thread2.start();
		try{
			Thread.currentThread().sleep(1000);
		}catch(InterruptedException e){
			
		}
		System.out.println(Thread.currentThread());
	}

}
