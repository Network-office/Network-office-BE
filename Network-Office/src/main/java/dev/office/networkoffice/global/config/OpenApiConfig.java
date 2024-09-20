package dev.office.networkoffice.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${swagger.server.dev}")
    private String devServerUrl;

    @Bean
    public OpenAPI createOpenApi() {
        return new OpenAPI()
                .info(getInfo())
                .addServersItem(getLocalServer())
                .addServersItem(getDevServer())
                .components(new Components());
    }

    private Info getInfo() {
        return new Info()
                .title("Network-Office API Documents")
                .version("v1")
                .description("Network-Office API 명세");
    }

    private Server getLocalServer() {
        return new Server().url("http://localhost:8080")
                .description("Local Server");
    }

    private Server getDevServer() {
        return new Server().url(devServerUrl)
                .description("Dev Server");
    }
}
