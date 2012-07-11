package test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestJmx {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext-bean.xml");
		Thread.currentThread().sleep(999999999);
	}

}
