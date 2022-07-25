package net.pilseong.demo.webserver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import net.pilseong.demo.entity.Order;

public abstract class OrderHandler implements HttpHandler {
  private ObjectMapper objectMapper = new ObjectMapper();
  private HttpExchange exchange;

  public abstract void get(Order order, HttpExchange exchange) throws IOException;
  
  public abstract void get(List<Order> orders, HttpExchange exchange) throws IOException;

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    this.exchange = exchange;

    // fetch request information
    String method = exchange.getRequestMethod();
    URI uri = exchange.getRequestURI();
    String path = uri.getPath();
    String content = "";

    // logging out to the console with URI info
    // System.out.println(String.format("Method: %s, path: %s", method, path));

    // get the body from the request
    String body = this.getBody(exchange.getRequestBody());

    if (body.isBlank()) {
      content = "Order information is not included";
      exchange.sendResponseHeaders(404, content.getBytes().length);
      OutputStream outputStream = exchange.getResponseBody();
      outputStream.write(content.getBytes());
      outputStream.flush();
      outputStream.close();
      return;
    }

    // make the JSON to Order object
    Order order = null;
    List<Order> orders = null;
    try {
      if (body.contains("[")) {
        orders = toOrders(body);
      } else {
        order = toOrder(body); 
      }
    } catch (Exception ex) {
      System.out.println("foramt error");
    }
    
    // we deal with POST here only
    if (method.equals("POST") && path.equals("/order")) {
      if (order != null) {
        this.get(order, exchange);  
      } else {
        this.get(orders, exchange);
      }
    } else {
      content = "Only POST method is supported";
      exchange.sendResponseHeaders(404, content.getBytes().length);
      OutputStream outputStream = exchange.getResponseBody();
      outputStream.write(content.getBytes());
      outputStream.flush();
      outputStream.close();
      return;
    }
  }

  protected void send(int status, String content) throws IOException {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "text/html");
      exchange.sendResponseHeaders(status, content.getBytes().length);
      exchange.getResponseBody().write(content.getBytes());
      exchange.getResponseBody().flush();
      exchange.getResponseBody().close();
  }

  protected String getBody(InputStream inputStream) {
    return new BufferedReader(new InputStreamReader(inputStream))
        .lines()
        .collect(Collectors.joining("\n"));
  }

  protected Order toOrder(String content) throws JsonProcessingException {
    return objectMapper.readValue(content, Order.class);
  }
  
  protected List<Order> toOrders(String content) throws JsonProcessingException {
    Order[] orders =  objectMapper.readValue(content, Order[].class);
    return Arrays.asList(orders); 
  }


  // printing to JSON data to the console
  protected String orderToJSON(Order order) throws IOException {
    OutputStream outputStream = new ByteArrayOutputStream();
    objectMapper.writeValue(outputStream, order);

    return outputStream.toString();
  }
  
  protected String ordersToJSON(List<Order> orders) throws IOException {
    OutputStream outputStream = new ByteArrayOutputStream();
    objectMapper.writeValue(outputStream, orders);

    return outputStream.toString();
  }

}
