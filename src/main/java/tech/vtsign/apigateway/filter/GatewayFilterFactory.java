package tech.vtsign.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tech.vtsign.apigateway.exception.RequestException;
import tech.vtsign.apigateway.service.WebClientService;

import java.util.List;

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
            List<String> acceptURL = List.of("/v3/api-docs", "/user/activation", "/document/apt/");
            boolean moveOn = isAcceptURL(acceptURL, path);
            if (!moveOn) {
                String bearerToken = request.getHeaders().getFirst("Authorization");
                if (bearerToken == null) {
                    return Mono.error(new RequestException(HttpStatus.BAD_REQUEST, "Missing access token"));
                }

                Mono<?> tokenMono = webClientService.getJWTToken("/auth/jwt", bearerToken);
                return tokenMono.map(token -> {
                    exchange.getRequest()
                            .mutate()
                            .headers(
                                    h -> h.set("Authorization", "Bearer " + token))
                            .build();
                    return exchange;
                }).flatMap(chain::filter);
            } else return chain.filter(exchange);
        };
    }

    private boolean isAcceptURL(List<String> acceptURL, String path) {
        boolean flag = false;
        for (String url : acceptURL) {
            if (path.contains(url)) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    public static class Config {

    }

}
