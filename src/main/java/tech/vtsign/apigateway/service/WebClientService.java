package tech.vtsign.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.vtsign.apigateway.exception.ExceptionResponse;
import tech.vtsign.apigateway.exception.RequestException;

@Service
@RequiredArgsConstructor
public class WebClientService {

    private final WebClient.Builder builder;

    public Mono<String> getJWTToken(String uri, String bearerToken) {
        return this.builder.build().post()
                .uri("http://auth-service/" + uri)
                .header("Authorization", bearerToken)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> {
                            Mono<ExceptionResponse> exceptionResponseMono = error.bodyToMono(ExceptionResponse.class);
                            return exceptionResponseMono
                                    .flatMap(exceptionResponse ->
                                            Mono.error(new RequestException(HttpStatus.valueOf(exceptionResponse.getStatus()),
                                                    exceptionResponse.getMessage())));
                        })
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Service unavailable")))
                .bodyToMono(String.class);
    }

}