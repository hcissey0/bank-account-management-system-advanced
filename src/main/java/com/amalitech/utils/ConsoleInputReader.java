package com.amalitech.utils;

import java.util.Scanner;

/** Console-based implementation of InputReader using Scanner. */
public class ConsoleInputReader implements InputReader, AutoCloseable {

  private final Scanner scanner;

  /** Creates a new console input reader with System.in. */
  public ConsoleInputReader() {
    this.scanner = new Scanner(System.in);
  }

  @Override
  public void waitForEnter() {
    System.out.println("\nPress Enter to continue...");
    scanner.nextLine();
  }

  @Override
  public int readInt(String prompt, int min, int max) {
    int value;
    while (true) {
      System.out.print(prompt);
      try {
        value = Integer.parseInt(scanner.nextLine().trim());
        if (value >= min && value <= max) {
          break;
        } else {
          System.out.println(
              "Invalid input. Please enter a number between " + min + " and " + max + ".");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
    return value;
  }

  @Override
  public double readDouble(String prompt, double min) {
    double value;
    while (true) {
      System.out.print(prompt);
      try {
        value = Double.parseDouble(scanner.nextLine().trim());
        if (value >= min) {
          break;
        } else {
          System.out.println(
              "Invalid input. Please enter a number greater than or equal to " + min + ".");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
    return value;
  }

  @Override
  public String readString(String prompt) {
    String value;
    while (true) {
      System.out.print(prompt);
      value = scanner.nextLine().trim();
      if (!value.isEmpty()) {
        break;
      } else {
        System.out.println("Input cannot be empty. Please try again.");
      }
    }
    return value;
  }

  @Override
  public void close() {
    if (scanner != null) {
      scanner.close();
    }
  }
}
