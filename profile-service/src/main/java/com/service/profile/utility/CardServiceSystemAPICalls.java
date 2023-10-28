package com.service.profile.utility;

import com.service.profile.converter.SystemDTOConverter;
import com.service.profile.profiles.Card;
import com.service.profile.profiles.Guardian;
import com.service.profile.profiles.User;
import com.service.profile.systemDTOs.Bundle;
import com.service.profile.systemDTOs.CardDataTransfer;
import com.service.profile.systemDTOs.GuardianDataTransfer;
import com.service.profile.systemDTOs.UserDataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class CardServiceSystemAPICalls {
    /*
    Static classes are not managed by the Spring container, which typically handles dependency injection.
    So, normal constructor or field injection won't work here. We explicitly need to create a bean of this
    class using @Component and then do setter-method injection.
     */
    static private RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        CardServiceSystemAPICalls.restTemplate = restTemplate;
    }

    public static void updateUserPasswordInCardService(User savedUser) {
        UserDataTransfer data = SystemDTOConverter.fromUserToUserResponse(savedUser);
        String cardServiceURL = "http://localhost:8081/system/update-user-password";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDataTransfer> httpRequest = new HttpEntity<>(data, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(cardServiceURL, httpRequest, String.class);
    }
    public static void updateGuardianPasswordInCardService(Guardian savedGuardian) {
        GuardianDataTransfer data = SystemDTOConverter.fromGuardianToGuardianResponse(savedGuardian);
        String cardServiceURL = "http://localhost:8081/system/update-guardian-password";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GuardianDataTransfer> httpRequest = new HttpEntity<>(data, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(cardServiceURL, httpRequest, String.class);
    }
    public static void sendEntitiesToCardService(Guardian guardian, User user, Card card) {
        GuardianDataTransfer guardianDataTransfer = SystemDTOConverter.fromGuardianToGuardianResponse(guardian);
        UserDataTransfer userDataTransfer = SystemDTOConverter.fromUserToUserResponse(user);
        CardDataTransfer cardDataTransfer = SystemDTOConverter.fromCardToCardResponse(card);
        Bundle bundle = Bundle.builder()
                .guardianDataTransfer(guardianDataTransfer)
                .userDataTransfer(userDataTransfer)
                .cardDataTransfer(cardDataTransfer)
                .build();

        String cardServiceURL = "http://localhost:8081/system/add-entities";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Bundle> request = new HttpEntity<>(bundle, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(cardServiceURL, request, String.class);
    }
    public static void removeChildFromCardService(Guardian savedGuardian, String childUsername) {
        GuardianDataTransfer data = SystemDTOConverter.fromGuardianToGuardianResponse(savedGuardian);
        String cardServiceURL = "http://localhost:8081/system/remove-child?username="+childUsername;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GuardianDataTransfer> request = new HttpEntity<>(data, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(cardServiceURL, request, String.class);
    }
}
