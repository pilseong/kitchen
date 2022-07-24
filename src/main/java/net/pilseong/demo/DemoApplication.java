package net.pilseong.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.pilseong.demo.webserver.WebServer;

@SpringBootApplication
public class DemoApplication {
	
	private OrderManager orderManager;
	private WebServer webServer;

	public DemoApplication(OrderManager orderManager, WebServer webServer) {
		this.orderManager = orderManager;
		this.webServer = webServer;
	}
	
	public void start() {

		Thread orderManagerThread = new Thread(orderManager);
		orderManagerThread.setName("Kitchen Manager");
		orderManagerThread.start();

		this.webServer.start();
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(DemoApplication.class);
		DemoApplication demoApplication = (DemoApplication) ctx.getBean("demoApplication");
		demoApplication.start();
		((ConfigurableApplicationContext)ctx).close();
	}
}
