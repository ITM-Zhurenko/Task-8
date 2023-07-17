package app.service;

import app.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import java.util.Objects;

@Service
public class UserService {

    private final WebClient webClient;

    @Autowired
    public UserService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String doRequestChain() {
        ResponseSpec responseSpec = webClient.get()
                .retrieve();
        final String[] sessionCookie = getCookies(responseSpec);
        final User user = createUser();
        final String firstPart = webClient
                .post()
                .bodyValue(user)
                .cookie(sessionCookie[0], sessionCookie[1])
                .retrieve()
                .bodyToMono(String.class)
                .block();
        user.setName("Thomas");
        user.setLastName("Shelby");
        String secondPart = webClient
                .put()
                .bodyValue(user)
                .cookie(sessionCookie[0], sessionCookie[1])
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String thirdPart = webClient
                .delete()
                .uri("/3")
                .cookie(sessionCookie[0], sessionCookie[1])
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return firstPart + secondPart + thirdPart;
    }

    private String[] getCookies(ResponseSpec responseSpec) {
        final String cookiesString = Objects.requireNonNull(responseSpec.toBodilessEntity()
                        .block())
                .getHeaders()
                .getFirst(HttpHeaders.SET_COOKIE);
        assert cookiesString != null;
        String[] cookiesArray = cookiesString.split(";");

        return cookiesArray[0].split("=");
    }

    private User createUser() {
        return new User(3L, "James", "Brown", (byte) 30);
    }
}
