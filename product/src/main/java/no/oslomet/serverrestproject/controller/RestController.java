package no.oslomet.serverrestproject.controller;

import no.oslomet.serverrestproject.model.Product;
import no.oslomet.serverrestproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public String home(){
        return "This is the product rest controller";
    }

    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable long id){
        return productService.getProductById(id);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProductById(@PathVariable long id){
        productService.deleteProductById(id);
    }

    @DeleteMapping("/products")
    public void deleteAllProducts(){
        productService.deleteAllProducts();
    }

    @PostMapping("/products")
    public Product saveProduct( @RequestBody  Product newProduct){
        return productService.saveProduct(newProduct);
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable long id,  @RequestBody  Product newProduct){
        newProduct.setId(id);
        return productService.saveProduct(newProduct);
    }


    /* @PostMapping("/products/order/{id}")
    public Product orderProduct( @RequestBody  User newUser, @PathVariable long id){
        return t;
    }
    */
}
