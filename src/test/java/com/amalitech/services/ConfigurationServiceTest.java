package com.amalitech.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigurationServiceTest {

  private static final String TEST_CONFIG_FILE = "target/test-config.txt";
  private ConfigurationService configService;

  @BeforeEach
  void setUp() throws IOException {
    // Ensure clean state
    Files.deleteIfExists(Paths.get(TEST_CONFIG_FILE));
    configService = new ConfigurationService(TEST_CONFIG_FILE);
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.deleteIfExists(Paths.get(TEST_CONFIG_FILE));
  }

  @Test
  void testConstructor() {
    ConfigurationService configurationService = new ConfigurationService();
    assertNotNull(configurationService, "ConfigurationService instance should not be null");
  }

  @Test
  void testDefaultValues() {
    // When file doesn't exist, defaults should be true
    assertTrue(configService.isAutoSave(), "AutoSave should be true by default");
    assertTrue(configService.isAutoLoadOnStartup(), "AutoLoad should be true by default");
    assertTrue(configService.isSaveOnExit(), "SaveOnExit should be true by default");

    // Verify file was created with defaults
    assertTrue(
        Files.exists(Paths.get(TEST_CONFIG_FILE)), "Config file should be created with defaults");
  }

  @Test
  void testPersistence() {
    // Change values
    configService.setAutoSave(false);
    configService.setAutoLoadOnStartup(false);
    configService.setSaveOnExit(false);

    // Create new instance to reload from file
    ConfigurationService newService = new ConfigurationService(TEST_CONFIG_FILE);

    assertFalse(newService.isAutoSave(), "AutoSave should be persisted as false");
    assertFalse(newService.isAutoLoadOnStartup(), "AutoLoad should be persisted as false");
    assertFalse(newService.isSaveOnExit(), "SaveOnExit should be persisted as false");
  }

  @Test
  void testToggleSettings() {
    assertTrue(configService.isAutoSave());
    configService.setAutoSave(false);
    assertFalse(configService.isAutoSave());

    assertTrue(configService.isAutoLoadOnStartup());
    configService.setAutoLoadOnStartup(false);
    assertFalse(configService.isAutoLoadOnStartup());

    assertTrue(configService.isSaveOnExit());
    configService.setSaveOnExit(false);
    assertFalse(configService.isSaveOnExit());
  }
}
