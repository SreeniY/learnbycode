package yetchina.threads;

public class DeadLockExample {
	String o1 = "Lock";
	String o2 = "Step";
	Thread t1 = (new Thread("Printer1"){
		public void run(){
			while(true){
				synchronized(o1){
					synchronized(o2){
						System.out.println(o1+o2);
					}
				}
			}
		}
	});
	Thread t2 = (new Thread("Printer2"){
		public void run(){
			while(true){
				synchronized(o2){
					synchronized(o1){
						System.out.println(o2+o1);
					}
				}
			}
		}
	});
	public static void main(String[] args){
		DeadLockExample dLock = new DeadLockExample();
		dLock.t1.start();
		dLock.t2.start();
	}

}
