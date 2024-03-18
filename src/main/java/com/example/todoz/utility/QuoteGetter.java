package com.example.todoz.utility;

import com.example.todoz.dtos.QuoteDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class QuoteGetter {
    private final WebClient webClient;
    @Value("${api.ninjas.api.key}")
    private String apiKey;
    public static QuoteDTO quote = new QuoteDTO("This app is awesome *nervous wink*", "DEV Tom");

    public QuoteGetter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> fetchQuote() {
        return webClient.get()
                .uri("https://api.api-ninjas.com/v1/quotes?category=success")
                .header("X-Api-Key", apiKey)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Scheduled (cron = "0 30 3 * * *")
    public void getRandomQuote() {
        do {
            quote = fetchQuote()
                    .flatMap(this::convertJsonArray)
                    .block();
        }
        while (quote != null && quote.quote().length() > 150);
    }

    public Mono<QuoteDTO> convertJsonArray(String jsonArray) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode attributes = objectMapper.readTree(jsonArray).get(0);
                return Mono.just(new QuoteDTO(
                        attributes.path("quote").asText(),
                        attributes.path("author").asText()
                ));
        } catch (JsonProcessingException e) {
            return Mono.empty();
        }
    }
}