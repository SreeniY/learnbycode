/**
 * 
 */
package yetchina.threads;

/**
 * @author usr1
 *
 */
public class MyThread1 {
	public static void main(String[] args) {
		A threadA = new A();
		B threadB = new B();
		C threadC = new C();
		
		threadA.start();
		threadB.start();
		threadC.start();
	}

}
class A extends Thread{
	public void run(){
		for(int i=1; i<=5; i++){
			System.out.println("From Thread A: i = "+i);
		}
		System.out.println("Exited from thread A");
	}
}

class B extends Thread{
	public void run(){
		for(int j=1; j<=5; j++){
			System.out.println("From Thread B: j = "+j);
		}
		System.out.println("Exiting from thread B");
	}
}

class C extends Thread{
	public void run(){
		for(int k=1;k<=5;k++){
			System.out.println("From Thread C: k = "+k);
		}
		System.out.println("Exiting from thread C");
	}
}
