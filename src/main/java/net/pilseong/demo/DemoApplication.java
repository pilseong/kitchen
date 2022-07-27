package net.pilseong.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.pilseong.demo.order.OrderDispatcher;
import net.pilseong.demo.webserver.WebServer;

@SpringBootApplication
public class DemoApplication {
	
	private OrderDispatcher orderDispatcher;
	private WebServer webServer;

	public DemoApplication(OrderDispatcher orderDispatcher, 
		WebServer webServer) {
		this.orderDispatcher = orderDispatcher;
		this.webServer = webServer;
		
		this.start();
	}
	
	public void start() {

		Thread orderDispatcherThread = new Thread(orderDispatcher);

		orderDispatcherThread.setName("Order Manager");
		orderDispatcherThread.start();

		this.webServer.start();
	}

	public static void main(String[] args) {
	  SpringApplication.run(DemoApplication.class, args);
	  
	  
//	  for legacy spring prject settigs
//		ApplicationContext ctx = new AnnotationConfigApplicationContext(DemoApplication.class);
//		DemoApplication demoApplication = (DemoApplication) ctx.getBean("demoApplication");
//		demoApplication.start();
//		((ConfigurableApplicationContext)ctx).close();
	}
}
