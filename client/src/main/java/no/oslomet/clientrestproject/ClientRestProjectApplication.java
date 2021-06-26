package no.oslomet.clientrestproject;

import no.oslomet.clientrestproject.controller.MainController;
import no.oslomet.clientrestproject.model.User;
import no.oslomet.clientrestproject.service.OrderService;
import no.oslomet.clientrestproject.service.ProductService;
import no.oslomet.clientrestproject.service.ShippingService;
import no.oslomet.clientrestproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;


@SpringBootApplication
@ComponentScan({"no.oslomet.clientrestproject", "no.oslomet.clientrestproject.controller"})
public class ClientRestProjectApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShippingService shippingService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public static void main(String[] args) {
        new File(MainController.uploadDirectory).mkdir();
        SpringApplication.run(ClientRestProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        String pass1 = bCryptPasswordEncoder.encode("admin");

        User admin = new User("admin", "admin", "admin@admin.com", pass1, "ADMIN");

        userService.saveUser(admin);
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
