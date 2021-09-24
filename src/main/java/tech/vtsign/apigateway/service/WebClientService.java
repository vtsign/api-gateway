package tech.vtsign.apigateway.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.vtsign.apigateway.proxy.JwtResponse;

@Service
public class WebClientService {

    private final WebClient webClient;

    public WebClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8100").build();
    }

    public Mono<JwtResponse> someRestCall(String name) {
        return this.webClient.post().uri("/"+name)
                .retrieve().bodyToMono(JwtResponse.class);
    }
}