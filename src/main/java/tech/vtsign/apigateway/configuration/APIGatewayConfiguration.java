package tech.vtsign.apigateway.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.vtsign.apigateway.filter.AuthGatewayFilterFactory;
import tech.vtsign.apigateway.filter.ServiceGatewayFilterFactory;

@Configuration
@RequiredArgsConstructor
public class APIGatewayConfiguration {
    private final ServiceGatewayFilterFactory serviceGatewayFilterFactory;
    private final AuthGatewayFilterFactory authGatewayFilterFactory;

    @Bean
    public RouteLocator gatewayRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/user/**")
                        .filters(f -> f.filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config())))
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
