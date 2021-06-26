package no.oslomet.clientrestproject.service;

import no.oslomet.clientrestproject.model.Shipping;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingService {

    String BASE_URL = "http://localhost:7070/shippings";
    private RestTemplate restTemplate = new RestTemplate();

    public List<Shipping> getAllShippings() {
        return Arrays.stream(
                restTemplate.getForObject(BASE_URL, Shipping[].class)
        ).collect(Collectors.toList());
    }

    public Shipping getShippingById(long id) {
        Shipping shipping = restTemplate.getForObject(BASE_URL + "/" + id, Shipping.class);
        return shipping;
    }

    public Shipping saveShipping(Shipping newShipping, Long userId) {
        return restTemplate.postForObject(BASE_URL +'/'+userId, newShipping, Shipping.class);
    }

    public void updateShipping(long id, Shipping updatedShipping) {
        restTemplate.put(BASE_URL + "/" + id, updatedShipping);
    }

    public void deleteShipping(long id) {
        restTemplate.delete(BASE_URL + "/" + id);
    }

    public void deleteAllShippings(){
        restTemplate.delete(BASE_URL);
    }
}
