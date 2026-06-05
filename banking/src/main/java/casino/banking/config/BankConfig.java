package casino.banking.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BankConfig {

    @Bean
    RestClient bankRestClient(
            @Value("${casino.services.bank.baseURL}") String bankBaseUrl
    ) {
        return RestClient.builder()
                .baseUrl(bankBaseUrl)
                .build();
    }

    @Bean
    RestClient rouletteRestClient(
            @Value("${casino.services.roulette.baseURL}") String bankBaseUrl
    ) {
        return RestClient.builder()
                .baseUrl(bankBaseUrl)
                .build();
    }

    @Bean
    RestClient slotsRestClient(
            @Value("${casino.services.slots.baseURL}") String bankBaseUrl
    ) {
        return RestClient.builder()
                .baseUrl(bankBaseUrl)
                .build();
    }
}
