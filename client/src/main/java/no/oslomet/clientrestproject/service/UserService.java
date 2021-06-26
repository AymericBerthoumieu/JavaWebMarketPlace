package no.oslomet.clientrestproject.service;

import no.oslomet.clientrestproject.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    String BASE_URL = "http://localhost:7070/users";
    private RestTemplate restTemplate = new RestTemplate();

    public List<User> getAllUsers() {
        return Arrays.stream(
                restTemplate.getForObject(BASE_URL, User[].class)
        ).collect(Collectors.toList());
    }

    public User getUserByEmail(String email) {
        User user = restTemplate.getForObject(BASE_URL + "/email/" + email, User.class);
        return user;
    }

    public User saveUser(User newUser) {
        return restTemplate.postForObject(BASE_URL, newUser, User.class);
    }

    public void updateUser(String email, User updatedUser) {
        restTemplate.put(BASE_URL + "/" + email, updatedUser);
    }

    public void updateNameUser(String email, User updatedUser) {
        restTemplate.put(BASE_URL + "/changeName/" + email, updatedUser);
    }

    public void updateEmailUser(String email, User updatedUser) {
        restTemplate.put(BASE_URL + "/changeEmail/" + email, updatedUser);
    }

    public void updatePWUser(String email, User updatedUser) {
        restTemplate.put(BASE_URL + "/changePW/" + email, updatedUser);
    }

    public void deleteUser(long id) {
        restTemplate.delete(BASE_URL + "/" + id);
    }

    public void deleteAllUsers(){
        restTemplate.delete(BASE_URL);
    }
}
