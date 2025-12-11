package com.amalitech.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/** Manages application configuration settings. */
public class ConfigurationService {

  private static final String DEFAULT_CONFIG_FILE = "src/main/resources/config.txt";
  private static final String KEY_AUTO_SAVE = "auto_save";
  private static final String KEY_AUTO_LOAD = "auto_load_on_startup";
  private static final String KEY_SAVE_ON_EXIT = "save_on_exit";

  private final String configFile;
  private final Properties properties;

  public ConfigurationService() {
    this(DEFAULT_CONFIG_FILE);
  }

  public ConfigurationService(String configFile) {
    this.configFile = configFile;
    this.properties = new Properties();
    loadConfig();
  }

  private void loadConfig() {
    Path path = Paths.get(configFile);
    if (Files.exists(path)) {
      try (FileInputStream in = new FileInputStream(configFile)) {
        properties.load(in);
      } catch (IOException e) {
        System.err.println("Warning: Could not load config file. Using defaults.");
      }
    } else {
      // Set defaults if file doesn't exist
      properties.setProperty(KEY_AUTO_SAVE, "true");
      properties.setProperty(KEY_AUTO_LOAD, "true");
      properties.setProperty(KEY_SAVE_ON_EXIT, "true");
      saveConfig();
    }
  }

  public void saveConfig() {
    try {
      Path path = Paths.get(configFile);
      if (path.getParent() != null && !Files.exists(path.getParent())) {
        Files.createDirectories(path.getParent());
      }
      try (FileOutputStream out = new FileOutputStream(configFile)) {
        properties.store(out, "Bank Account Management System Configuration");
      }
    } catch (IOException e) {
      System.err.println("Error saving config file: " + e.getMessage());
    }
  }

  public boolean isAutoSave() {
    return Boolean.parseBoolean(properties.getProperty(KEY_AUTO_SAVE, "true"));
  }

  public void setAutoSave(boolean autoSave) {
    properties.setProperty(KEY_AUTO_SAVE, String.valueOf(autoSave));
    saveConfig();
  }

  public boolean isAutoLoadOnStartup() {
    return Boolean.parseBoolean(properties.getProperty(KEY_AUTO_LOAD, "true"));
  }

  public void setAutoLoadOnStartup(boolean autoLoad) {
    properties.setProperty(KEY_AUTO_LOAD, String.valueOf(autoLoad));
    saveConfig();
  }

  public boolean isSaveOnExit() {
    return Boolean.parseBoolean(properties.getProperty(KEY_SAVE_ON_EXIT, "true"));
  }

  public void setSaveOnExit(boolean saveOnExit) {
    properties.setProperty(KEY_SAVE_ON_EXIT, String.valueOf(saveOnExit));
    saveConfig();
  }
}
