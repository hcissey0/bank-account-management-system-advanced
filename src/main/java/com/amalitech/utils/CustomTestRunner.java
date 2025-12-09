package com.amalitech.utils;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class CustomTestRunner {

  public void runTests() {
    Launcher launcher = LauncherFactory.create();

    LauncherDiscoveryRequest request =
        LauncherDiscoveryRequestBuilder.request()
            .selectors(
                selectPackage("com.amalitech.accounts"),
                selectPackage("com.amalitech.customers"),
                selectPackage("com.amalitech.transactions"))
            .build();

    SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();

    TestExecutionListener listener =
        new TestExecutionListener() {
          @Override
          public void executionFinished(
              TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            if (testIdentifier.isTest()) {
              String testName = testIdentifier.getDisplayName();

              System.out.print("Test: " + testName);

              // Padding
              int padding = 50 - testName.length();
              for (int i = 0; i < padding; i++) {
                System.out.print(".");
              }

              if (testExecutionResult.getStatus() == TestExecutionResult.Status.SUCCESSFUL) {
                System.out.println("(PASSED)");
              } else {
                System.out.println("(FAILED)");
                testExecutionResult
                    .getThrowable()
                    .ifPresent(t -> System.out.println("   Error: " + t.getMessage()));
              }
            }
          }
        };

    launcher.registerTestExecutionListeners(listener, summaryListener);
    launcher.execute(request);

    TestExecutionSummary summary = summaryListener.getSummary();
    if (summary.getTestsFoundCount() == 0) {
      System.out.println(
          "No tests found. Please ensure test classes are compiled and in the classpath.");
    } else {
      System.out.println("\nTest Run Finished.");
      System.out.println("Tests found: " + summary.getTestsFoundCount());
      System.out.println("Tests passed: " + summary.getTestsSucceededCount());
      System.out.println("Tests failed: " + summary.getTestsFailedCount());
    }
  }
}
