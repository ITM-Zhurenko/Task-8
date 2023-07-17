package app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan(value = "app")
public class AppConfig {

    @Bean
    public WebClient getWebClient() {
        return WebClient
                .builder()
                .baseUrl("http://94.198.50.185:7081/api/users")
                .build();
    }
}
