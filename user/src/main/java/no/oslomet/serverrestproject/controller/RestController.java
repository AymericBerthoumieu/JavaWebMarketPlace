package no.oslomet.serverrestproject.controller;

import javafx.util.Pair;
import no.oslomet.serverrestproject.model.Order;
import no.oslomet.serverrestproject.model.Shipping;
import no.oslomet.serverrestproject.model.User;
import no.oslomet.serverrestproject.service.OrderService;
import no.oslomet.serverrestproject.service.ShippingService;
import no.oslomet.serverrestproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    ShippingService shippingService;


    @GetMapping("/")
    public String home(){
        return "This is the user rest controller";
    }


    /* ==================================== Order ==================================== */

    @GetMapping("/orders")
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable long id){
        return orderService.getOrderById(id);
    }

    @DeleteMapping("/orders")
    public void deleteAllOrders(){
        orderService.deleteAllOrders();
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrderById(@PathVariable long id){
        orderService.deleteOrderById(id);
    }

    @PostMapping("/orders")
    public Order saveOrder( @RequestBody  Order newOrder){
        return orderService.saveOrder(newOrder);
    }

    @PutMapping("/orders/{userId}")
    public Order updateOrder(@PathVariable long userId,  @RequestBody  Order newOrder){
        newOrder.setUser(userService.getUserById(userId));
        return orderService.saveOrder(newOrder);
    }

    @PutMapping("/orders/o/{productId}")
    public Order order(@RequestBody Long userId, @PathVariable long productId){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d =new Date();
        String date = df.format(d);


        User user = userService.getUserById(userId);
        List<Order> orders = user.getOrders();
        Order order = new Order();
        order.setDate(date);
        order.setUser(user);
        for (Order o : orders) {
            if (o.getShipping()==null){
                order = o;
            }
        }
        List<Long> products= new ArrayList<>();
        if (order.getProducts()!=null) {
            products = order.getProducts();
        }
        products.add(productId);
        order.setProducts(products);
        orderService.saveOrder(order);

        return order;
    }

    /* ==================================== User ==================================== */

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/email/{email}")
    public User getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable long id){
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public User saveUser( @RequestBody  User newUser){
        return userService.saveUser(newUser);
    }

    @PutMapping("/users/{email}")
    public User updateUser(@PathVariable String email,  @RequestBody  User newUser){
        long id = userService.getUserByEmail(email).getUser_id();
        newUser.setUser_id(id);
        return userService.saveUser(newUser);
    }

    @PutMapping("/users/changeName/{email}")
    public User updateNameUser(@PathVariable String email,  @RequestBody  User newUser){
        User user = userService.getUserByEmail(email);
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        return userService.saveUser(user);
    }

    @PutMapping("/users/changeEmail/{email}")
    public User updateEmailUser(@PathVariable String email,  @RequestBody  User newUser){
        User user = userService.getUserByEmail(email);
        user.setEmail(newUser.getEmail());
        return userService.saveUser(user);
    }

    @PutMapping("/users/changePW/{email}")
    public User updatePWUser(@PathVariable String email,  @RequestBody  User newUser){
        User user = userService.getUserByEmail(email);
        user.setPassword(newUser.getPassword());
        return userService.saveUser(user);
    }


    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable long id){
        userService.deleteUserById(id);
    }

    @DeleteMapping("/users")
    public void deleteUser(){
        userService.deleteAllUsers();
    }

    /* ==================================== Shipping ==================================== */

    @GetMapping("/shippings")
    public List<Shipping> getAllShippings(){
        return shippingService.getAllShippings();
    }

    @GetMapping("/shippings/{id}")
    public Shipping getShippingById(@PathVariable long id){
        return shippingService.getShippingById(id);
    }

    @PostMapping("/shippings/{userId}")
    public Shipping saveShipping( @RequestBody  Shipping newShipping, @PathVariable Long userId){
        User user =userService.getUserById(userId);
        newShipping.setUser(user);
        return shippingService.saveShipping(newShipping);
    }

    @PutMapping("/shippings/{id}")
    public Shipping updateShipping(@PathVariable long id,  @RequestBody  Shipping newShipping){
        newShipping.setShipping_id(id);
        return shippingService.saveShipping(newShipping);
    }

    @DeleteMapping("/shippings/{id}")
    public void deleteShippingById(@PathVariable long id){
        shippingService.deleteShippingById(id);
    }

    @DeleteMapping("/shippings")
    public void deleteAllShipping(){
        shippingService.deleteAllShippings();
    }






/*
    @PostMapping("/tickets/order/{id}")
    public Ticket orderTicket( @RequestBody  User newUser, @PathVariable long id){
        Ticket t=ticketService.getTicketById(id);
        User user= userService.getUserByEmail(newUser.getEmail());
        if (user==null) {userService.saveUser(newUser);}
        user = userService.getUserByEmail(newUser.getEmail());
        t.setUser(user);
        List<Ticket> list = user.getTickets();
        if (list==null) {list= new ArrayList<>();}

        list.add(t);
        user.setTickets(list);
        userService.saveUser(user);
        ticketService.saveTicket(t);
        return t;
    }
   */
}
