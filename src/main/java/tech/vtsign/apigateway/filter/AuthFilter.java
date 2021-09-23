package tech.vtsign.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Request path: {}", exchange.getRequest().getPath());
//        if(exchange.getRequest().getPath().equals("/user/login")) {
//            // call auth service to get access token
//            // return to client
//
//        } else {
//            // call auth service to get jwt
//            // pass jwt to destination service
//        }


        // always pass request from to client
                // filter


        return chain.filter(exchange);
    }
}
