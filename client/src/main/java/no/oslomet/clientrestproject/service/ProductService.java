package no.oslomet.clientrestproject.service;

import no.oslomet.clientrestproject.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    String BASE_URL = "http://localhost:9090/products";
    private RestTemplate restTemplate = new RestTemplate();

    public List<Product> getAllProducts() {
        return Arrays.stream(
                restTemplate.getForObject(BASE_URL, Product[].class)
        ).collect(Collectors.toList());
    }

    public Product getProductById(long id) {
        Product product = restTemplate.getForObject(BASE_URL + "/" + id, Product.class);
        return product;
    }

    public Product saveProduct(Product newProduct) {
        return restTemplate.postForObject(BASE_URL, newProduct, Product.class);
    }

    public void updateProduct(long id, Product updatedProduct) {
        restTemplate.put(BASE_URL + "/" + id, updatedProduct);
    }

    public void deleteProduct(long id) {
        restTemplate.delete(BASE_URL + "/" + id);
    }

    public void deleteAllProducts(){
        restTemplate.delete(BASE_URL);
    }
}
