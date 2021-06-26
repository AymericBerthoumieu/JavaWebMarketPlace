package no.oslomet.serverrestproject.service;

import no.oslomet.serverrestproject.model.Order;
import no.oslomet.serverrestproject.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(long id){
        return orderRepository.findById(id).get();
    }

    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }

    public void deleteOrderById(long id){
        orderRepository.deleteById(id);
    }

    public void deleteAllOrders(){
        orderRepository.deleteAll();
    }
}
