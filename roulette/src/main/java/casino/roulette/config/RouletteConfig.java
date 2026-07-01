package casino.roulette.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RouletteConfig {

    @Bean
    RestClient bankRestClient(
            @Value("${casino.services.bank.baseURL}") String bankBaseUrl
    ) {
        return RestClient.builder()
                .baseUrl(bankBaseUrl)
                .build();
    }
}
