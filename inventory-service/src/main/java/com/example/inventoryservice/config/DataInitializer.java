package com.example.inventoryservice.config;

import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void run(String... args) throws Exception {
        inventoryService.initializeInventory();
        System.out.println("✅ Inventory initialized with sample products");
    }
}
