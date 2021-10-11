package tech.vtsign.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tech.vtsign.apigateway.service.WebClientService;

@Component
public class GatewayFilterFactory extends AbstractGatewayFilterFactory<GatewayFilterFactory.Config> {

    private final WebClientService webClientService;

    public GatewayFilterFactory(WebClientService webClientService) {
        super(Config.class);
        this.webClientService = webClientService;
    }
    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();
            if (!path.contains("/v3/api-docs") && !path.contains("/user/activation")) {
                if (!request.getHeaders().containsKey("access_token")) {
                    return this.onError(exchange, HttpStatus.BAD_REQUEST);
                }

                String token = request.getHeaders().get("access_token").get(0);
                Mono<String> responseMono = webClientService.someRestCall("/auth/jwt", token);
                return responseMono.map(range -> {
                    exchange.getRequest()
                            .mutate()
                            .headers(h -> h.add("Authorization", "Bearer " + range))
                            .build();
                    return exchange;
                }).flatMap(chain::filter);
            } else return chain.filter(exchange);
        };
    }
    public static class Config {

    }


}
