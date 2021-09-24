package tech.vtsign.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tech.vtsign.apigateway.proxy.JwtResponse;
import tech.vtsign.apigateway.service.WebClientService;

@Component
public class MyGatewayFilterFactory extends AbstractGatewayFilterFactory<MyGatewayFilterFactory.Config>  {
    @Autowired
    private WebClientService webClientService;

    public MyGatewayFilterFactory(){
        super(Config.class);
    }

    public static class Config {
        // ...
    }


    private boolean isAuthorizationValid(String authorizationHeader) {
        boolean isValid = true;

        // Logic for checking the value
        return isValid;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus)  {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();



//            if (!request.getHeaders().containsKey("Authorization")) {
//                return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
//            };
//
//            String authorizationHeader = request.getHeaders().get("Authorization").get(0);
//
//            if (!this.isAuthorizationValid(authorizationHeader)) {
//                return this.onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
//            }
            Mono<JwtResponse> responseMono = webClientService.someRestCall("signin");
            return responseMono.map(range ->{
                exchange.getRequest()
                        .mutate()
                        .headers(h -> h.add("Authorization",range.getJwt()))
                        .build();
                return exchange;
            }).flatMap(chain::filter);


//
        };
    }


}
