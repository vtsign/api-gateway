package tech.vtsign.apigateway.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tech.vtsign.apigateway.filter.GatewayFilterFactory;

@Configuration
@RequiredArgsConstructor
public class APIGatewayConfiguration {
    private final GatewayFilterFactory gatewayFilterFactory;
    private final Environment env;

    @Bean
    public RouteLocator gatewayRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/user/**")
                        .filters(f -> f.filter(gatewayFilterFactory.apply(new GatewayFilterFactory.Config())))
                        .uri("lb://user-service")
                )
                .route(r -> r.path("/document/**")
                        .filters(f -> f.filter(gatewayFilterFactory.apply(new GatewayFilterFactory.Config())))
                        .uri("lb://document-service")
                )
                .route(r -> r.path("/auth/**")
                        .uri("lb://auth-service")
                )
                .route(r -> r.path("/notification/**")
                        .filters(f -> f.filter(gatewayFilterFactory.apply(new GatewayFilterFactory.Config())))
                        .uri("lb://notification-service")
                )
                .route(r -> r.path("/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/${path}/v3/api-docs/"))
                        .uri(String.format("http://localhost:%s", env.getProperty("server.port")))
                )
                .build();
    }
}
