package com.salonbooking.userservice.service;

import com.salonbooking.userservice.payload.dto.*;
import com.salonbooking.userservice.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String KEYCLOAK_BASE_URL = "http://localhost:8080";
    private static final String KEYCLOAK_ADMIN_API = KEYCLOAK_BASE_URL + "/admin/realms/master/users";

    private static final String TOKEN_URL = KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/token";
    private static final String CLIENT_ID = "salon-booking-client";
    private static final String CLIENT_SECRET = "aR21h3EVOj9dtqH0rNRL5iQFn0i6H6UVmAHfFIrzsTkUjm2upktcEC6L2bqpriMEKkoOZIqGGorYzulbOr3EGY";
    private static final String GRANT_TYPE = "password";
    private static final String scope = "openid profile email";
    private static final String username = "remarkable";
    private static final String password = "admin";
    private static final String clientId = "2a5a34a8-6beb-40b2-be68-6b140d674bcc";

    private final RestTemplate restTemplate;

    public void createUser(SignUpDTO signUpDTO) throws  Exception{

        String ACCESS_TOKEN = getAdminAccessToken(username,password,GRANT_TYPE,null)
                .getAccessToken();

        Credential credential = new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(signUpDTO.getPassword());

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(signUpDTO.getUsername());
        userRequest.setEmail(signUpDTO.getEmail());
        userRequest.setEnabled(true);
        userRequest.setLastName(signUpDTO.getFullName());
        userRequest.setFirstName(signUpDTO.getFullName());
        userRequest.getCredentials().add(credential);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        HttpEntity<UserRequest> requestHttpEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KEYCLOAK_ADMIN_API,
                HttpMethod.POST,
                requestHttpEntity,
                String.class
        );

        if(response.getStatusCode() == HttpStatus.CREATED){
            System.out.println("User created successfully");

            KeyCloakUserDTO user = fetchFirstUserByUsername(signUpDTO.getUsername(),ACCESS_TOKEN);
            KeyCloakRole role = getRoleByName(clientId,ACCESS_TOKEN,signUpDTO.getRole().toString());

            List<KeyCloakRole> roles = new ArrayList<>();
            roles.add(role);

            assignRoleToUser(user.getId(),clientId,roles,ACCESS_TOKEN);
        } else {
            System.out.println("User failed to create");
            throw  new Exception(response.getBody());
        }
    }

    public TokenResponse getAdminAccessToken(String username,
                                             String password,
                                             String grantType,
                                             String refreshToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("username", username);
        requestBody.add("password", password);
        requestBody.add("refresh_token", refreshToken);
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("client_secret", CLIENT_SECRET);
        requestBody.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<>(requestBody, headers);

        System.out.println("Request Body: " + requestBody.toString());
        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                requestHttpEntity,
                TokenResponse.class
        );
        if(response.getStatusCode() == HttpStatus.OK && response.getBody()!= null){
            return response.getBody();
        }
        throw new Exception("Failed to obtain access token");

    }

    public KeyCloakRole getRoleByName(String clientId,
                                      String token,
                                      String role)  {

        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/clients/"+clientId+"/roles/"+role;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<KeyCloakRole> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestHttpEntity,
                KeyCloakRole.class
        );

        return response.getBody();

    }

    public KeyCloakUserDTO fetchFirstUserByUsername(String username,
                                                    String token) throws Exception {
        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/users?username="+username;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<KeyCloakUserDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestHttpEntity,
                KeyCloakUserDTO[].class
        );
        KeyCloakUserDTO[] users = response.getBody();
        if(users != null && users.length > 0){
            return users[0];
        }
        throw new Exception("User not found with username " + username);
    }

    public void assignRoleToUser(String userId,
                                 String clientId,
                                 List<KeyCloakRole> roles,
                                 String token) throws Exception {

        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/users/"+userId+"/role-mappings/clients/"+clientId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<KeyCloakRole>> requestHttpEntity = new HttpEntity<>(roles,headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestHttpEntity,
                    String.class
            );
        }catch (Exception e){
            throw  new Exception("Failed to assign new role " + e.getMessage());
        }
    }

    public KeyCloakUserDTO fetchUserProfileByJwt(String token) throws Exception {

        String url = KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestHttpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KeyCloakUserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestHttpEntity,
                    KeyCloakUserDTO.class
            );
            return response.getBody();
        }catch (Exception e) {
            throw new Exception("Failed to get user info " + e.getMessage());
        }
    }

}

