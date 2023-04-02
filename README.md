# FOOD DELIVERY SIMULATION (음식점 + 배달 시뮬레이션)

FOOD DELIVERY SIMULATION is a simple springboot program for measureing efficiencies of various delivery stategies. This program only relis on spring-boot-starter from Springboot framework and Jackson library for JSON parsing.

다양한 배달 전략의 효율성을 측정하는 단순한 스프링부트 프로그램이다. 스프링부트와 JSON 파싱 라이브러리만 의존한다.


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