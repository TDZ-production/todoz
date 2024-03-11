package com.example.todoz.utility;

import com.example.todoz.dtos.QuoteDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {
    private final WebClient webClient;
    @Value("${api.ninjas.api.key}")
    private String apiKey;

    public WebClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> fetchQuote() {
        return webClient.get()
                .uri("https://api.api-ninjas.com/v1/quotes?category=success")
                .header("X-Api-Key", apiKey)
                .retrieve()
                .bodyToMono(String.class).log();
    }

    public QuoteDTO getRandomQuote() {
        return fetchQuote()
                .flatMap(this::convertJsonArray)
                .onErrorResume(t -> Mono.empty())
                .retry()
                .block();
    }

    private Mono<QuoteDTO> convertJsonArray(String jsonArray) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode attributes = objectMapper.readTree(jsonArray).get(0);
                return Mono.just(new QuoteDTO(
                        attributes.path("quote").asText("quote"),
                        attributes.path("author").asText("author"),
                        attributes.path("category").asText("Unknown")
                ));
        } catch (JsonProcessingException e) {
            return Mono.empty();
        }
    }
}
