package tech.vtsign.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class WebClientService {

//    private final WebClient webClient;
//
//
//    public WebClientService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("http://localhost:8100").build();
//    }
//
//    public Mono<JwtResponse> someRestCall(String name) {
//        return this.webClient.post().uri("/"+name)
//                .retrieve().bodyToMono(JwtResponse.class);
//    }

    private final WebClient.Builder builder;

    public Mono<String> someRestCall(String uri, String accessToken) {
        return this.builder.build().post()
                .uri("http://auth-service/" + uri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve().bodyToMono(String.class);
    }

}