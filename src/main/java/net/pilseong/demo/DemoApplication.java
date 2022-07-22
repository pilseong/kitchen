package net.pilseong.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.pilseong.demo.kitchen.KitchenManager;
import net.pilseong.demo.webserver.WebServer;

@SpringBootApplication
public class DemoApplication {
	private KitchenManager kitchenManager;
	private WebServer webServer;

	public DemoApplication(KitchenManager kitchenManager, WebServer webServer) {
		this.kitchenManager = kitchenManager;
		this.webServer = webServer;
	}
	
	public void start() {
		// kitchenManager  = new KitchenManager(incommingOrderQueue)
		Thread kitchenManagerThread = new Thread(kitchenManager);
		kitchenManagerThread.setName("Kitchen Manager");
		kitchenManagerThread.start();

		// this.webServer = new WebServer(9900);
		// this.webServer.addHandler("/order", new OrderController());
		this.webServer.start();
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(DemoApplication.class);
		DemoApplication demoApplication = (DemoApplication) ctx.getBean("demoApplication");
		demoApplication.start();
	}
	
	// @Bean
  // public BlockingQueue<Order> incommingOrderQueue() {
  //   return new LinkedBlockingQueue<>();
  // }

  // @Bean
  // public KitchenManager kitchenManager() {
  //   return new KitchenManager(incommingOrderQueue());
  // }

  // @Bean
  // public CourierManager courierManager() {
	// 	return new CourierManager();
  // }
}
