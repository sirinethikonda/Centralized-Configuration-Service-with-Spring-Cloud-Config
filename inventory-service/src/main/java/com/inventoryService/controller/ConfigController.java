package com.inventoryService.controller;

import com.inventoryService.config.InventoryConfig;
import com.inventoryService.health.ConfigServerHealthIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RefreshScope
public class ConfigController {

    @Autowired
    private InventoryConfig inventoryConfig;

    @Autowired
    private ConfigServerHealthIndicator healthIndicator;

    // Reads the active profile (e.g., dev or prod). Defaults to 'default' if not found.
    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @GetMapping("/api/inventory/health")
    public Map<String, Object> getHealth() {
        var health = healthIndicator.health();
        boolean isUp = health.getStatus().equals(Status.UP);
        Object configDetail = health.getDetails().get("configServer");
        
        return Map.of(
            "status", isUp ? "UP" : "DOWN",
            "configServer", configDetail != null ? configDetail : "unreachable"
        );
    }

    // Required by assignment step 4: The endpoint to test properties
    @GetMapping("/api/inventory/config")
    public Map<String, Object> getConfig() {
        return Map.of(
            "profile", activeProfile,
            "maxStock", inventoryConfig.getMaxStock(),
            "replenishThreshold", inventoryConfig.getReplenishThreshold()
        );
    }
}
