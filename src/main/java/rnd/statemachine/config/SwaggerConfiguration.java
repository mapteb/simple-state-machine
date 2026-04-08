package rnd.statemachine.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi orderitemsApi() {
        return GroupedOpenApi.builder()
                .group("orders-api")
                .displayName("Orders APIs")
                .pathsToMatch("/api/orders/**")
                .build();
    }
}