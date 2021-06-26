package no.oslomet.clientrestproject.service;

import javafx.util.Pair;
import no.oslomet.clientrestproject.model.Order;
import no.oslomet.clientrestproject.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    String BASE_URL = "http://localhost:7070/orders";
    private RestTemplate restTemplate = new RestTemplate();

    public List<Order> getAllOrders() {
        return Arrays.stream(
                restTemplate.getForObject(BASE_URL, Order[].class)
        ).collect(Collectors.toList());
    }

    public Order getOrderById(long id) {
        Order order = restTemplate.getForObject(BASE_URL + "/" + id, Order.class);
        return order;
    }

    public Order saveOrder(Order newOrder) {
        return restTemplate.postForObject(BASE_URL, newOrder, Order.class);
    }

    public void updateOrder(Order updatedOrder, Long userId) {
        restTemplate.put(BASE_URL + "/" + userId, updatedOrder);
    }

    public void orderProduct(Long userId, Long id) {
        restTemplate.put(BASE_URL + "/o/"+ id , userId);
    }

    public void deleteOrder(long id) {
        restTemplate.delete(BASE_URL + "/" + id);
    }

    public void deleteAllOrders(){
        restTemplate.delete(BASE_URL);
    }
}
