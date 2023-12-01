# FOOD DELIVERY SIMULATION
## 음식점 + 배달 시뮬레이션

FOOD DELIVERY SIMULATION is a simple springboot program for measureing efficiencies of various delivery stategies. This program only relies on spring-boot-starter from Springboot framework and Jackson library for JSON parsing.

다양한 배달 전략의 효율성을 측정하는 단순한 스프링부트 프로그램이다. 스프링부트와 JSON 파싱 라이브러리만 의존한다. 
* 이 프로젝트 내에서는 아파치나 nginx같은 상용 웹서버를 사용하지 않는다. httpserver를 가지고 웹서버와 http handler, controller를 직접구현하였다.
* 이유는 이 프로그램의 경우 멀티 스레드를 통해 음식점의 상태와 음식점, 배달 상태, 주문 음식 조리의 상태, 배달부의 상태를 모두 관리해야 하기 때문에 간단한 휍서버를 구현하는 것이 기능구현을 위한 customization에서 훨씬 간단하고 유리하기 때문이다.
* 물론 이런 식으로 구현하면 tightly coupled된 구조가 되기 때문에 좋지 않다고 말할 수도 있겠지만, 어차피 프로토 타입이기 때문에 이런 고민은 무시한다.


## Build

Open as a gradle project and build with gradle build and start with bootRun task. Using IDE is also good option.

gradle 프로젝트로 gradle build 로 빌드한다.


```bash
gradle build
```

## Execution
Start with bootRun task. You can test with order.http REST client file or any rest clients making POST request with JSON array body.  

gradle bootRun으로 실행한다.

```bash
gradle bootRun
```

## Configuration
Set properties in application.properties file

설정은 application.properties로 한다.


```properties

# 'fifo' -> first-in, first-out strategy / 'matched' -> specific order designation
spring.profiles.active=fifo

# Tiny Webserver port
order.port=9900

# minreach, maxreact is the range of how much time in seconds 
# the courier takes to get to the kitchen
order.time.minreach=3
order.time.maxreach=18

# backpressure controls the speed of fetching order from webserver
order.time.backpressure=500

```

## Explanation
1. The server is going to print the stats out when the OrderDispatcher does not take orders more than 25 seconds
2. The simple webserver is implemented with com.sun.net.httpserver package and added JSON conversion functionalities to process requests with JSON body. So this server only can process POST reqeust with JSON body.
3. This program is based on the multi-threaded concurrency and the communication between threads has done throught the traditional heap memory access.
4. To make the program simple, there is OrderManager class which takes almost all status information and control the status of the order processing. It makes program simple as thread-safe manner.
5. OrderDispatcher is a consumer of order reqeusts from the producer, the webserver. The BlockingQueue is used for simple implementation. OrderDispatcher has the backpressure functionality because of this data structure.
6. OrderDispatcher registers orders to the status board and let CourierManager and KitchenManager know there is work to do. Observer pattern is not used here because only two observers exist. 
7. Instead Observer pattern used for letting to know the waiting courier that there is food ready to deliver.
8. First-In, First-Out and Matched Strategies for Courier fetching are implemented by loosely coupled CourierManager interface. It is possible to apply Template Method Pattern to reduce the boilerplate codes inside two different Courier Thread classes, but I have chose simpler approach.
9. order.http Rest Client file is included. If you use VS Code Rest client plugin, you can test it with this file.


## Author
Pilseong Heo (heops79@gmail.com)  

## License
NO

그런 거 없다
