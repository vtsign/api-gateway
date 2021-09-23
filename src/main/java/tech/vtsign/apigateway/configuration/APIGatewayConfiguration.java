package tech.vtsign.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIGatewayConfiguration {
    @Bean
    public RouteLocator gatewayRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/user/**")
                        .uri("lb://user-service")
                )
                .route(r -> r.path("/document/**")
                        .uri("lb://document-service")
                )
                .route(r -> r.path("/auth/**")
                        .uri("lb://auth-service")
                )
                .route(r -> r.path("/notification/**")
                        .uri("lb://notification-service")
                )
                .build();
    }
}
