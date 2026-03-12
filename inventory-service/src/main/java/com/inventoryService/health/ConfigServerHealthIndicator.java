package com.inventoryService.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConfigServerHealthIndicator implements HealthIndicator {

    @Value("${spring.cloud.config.uri:http://localhost:8888}")
    private String configServerUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Health health() {
        try {
            // Ping the Config Server's own health endpoint
            String response = restTemplate.getForObject(configServerUri + "/actuator/health", String.class);
            
            if (response != null && response.contains("\"status\":\"UP\"")) {
                // If it responds UP, return connected
                return Health.up().withDetail("configServer", "connected").build();
            } else {
                return Health.down().withDetail("configServer", "unreachable").build();
            }
        } catch (Exception e) {
            return Health.down().withDetail("configServer", "error connecting: " + e.getMessage()).build();
        }
    }
}
