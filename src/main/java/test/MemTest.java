package test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MemTest {
	public static void main(String[] args) {

		new ClassPathXmlApplicationContext("classpath:applicationContext-bean.xml");

	}
}
