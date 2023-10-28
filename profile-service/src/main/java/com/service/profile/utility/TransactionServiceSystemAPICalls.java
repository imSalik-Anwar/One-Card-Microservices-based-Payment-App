package com.service.profile.utility;

import com.service.profile.converter.SystemDTOConverter;
import com.service.profile.profiles.Card;
import com.service.profile.profiles.Guardian;
import com.service.profile.profiles.User;
import com.service.profile.systemDTOs.Bundle;
import com.service.profile.systemDTOs.CardDataTransfer;
import com.service.profile.systemDTOs.UserDataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class TransactionServiceSystemAPICalls {
    /*
Static classes are not managed by the Spring container, which typically handles dependency injection.
So, normal constructor or field injection won't work here. We explicitly need to create a bean of this
class using @Component and then do setter-method injection.
 */
    static private RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        TransactionServiceSystemAPICalls.restTemplate = restTemplate;
    }
    public static void sendEntitiesToCardService(Guardian guardian, User user, Card card) {
        UserDataTransfer userDataTransfer = SystemDTOConverter.fromUserToUserResponse(user);
        CardDataTransfer cardDataTransfer = SystemDTOConverter.fromCardToCardResponse(card);
        Bundle bundle = Bundle.builder()
                .userDataTransfer(userDataTransfer)
                .cardDataTransfer(cardDataTransfer)
                .build();

        String TransactionServiceURL = "http://localhost:8082/system/add-entities";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Bundle> request = new HttpEntity<>(bundle, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(TransactionServiceURL, request, String.class);
    }
    public static void removeChildFromTransactionService(String childUsername) throws IOException {
        String TransactionServiceURL = "http://localhost:8082/system/remove-child?username="+childUsername;
        // Create a URL object
        URL url = new URL(TransactionServiceURL);
        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Set the request method to DELETE
        connection.setRequestMethod("DELETE");
        // Get the response code
        int responseCode = connection.getResponseCode();
    }
    public static void updateUserPasswordInTransactionService(User user) throws IOException {
        String childUsername = user.getUsername();
        String password = user.getPassword();
        String TransactionServiceURL = "http://localhost:8082/system/update-password?username="+childUsername+"&password="+password;
        // Create a URL object
        URL url = new URL(TransactionServiceURL);
        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Set the request method to DELETE
        connection.setRequestMethod("PUT");
        // Get the response code
        int responseCode = connection.getResponseCode();
    }
}
