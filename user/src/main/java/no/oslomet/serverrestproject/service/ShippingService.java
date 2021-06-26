package no.oslomet.serverrestproject.service;

import no.oslomet.serverrestproject.model.Shipping;
import no.oslomet.serverrestproject.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingService {
    @Autowired
    private ShippingRepository shippingRepository;

    public List<Shipping> getAllShippings(){
        return shippingRepository.findAll();
    }

    public Shipping getShippingById(long id){
        return shippingRepository.findById(id).get();
    }

    public Shipping saveShipping(Shipping shipping){
        return shippingRepository.save(shipping);
    }

    public void deleteShippingById(long id){
        shippingRepository.deleteById(id);
    }

    public void deleteAllShippings(){
        shippingRepository.deleteAll();
    }
}
