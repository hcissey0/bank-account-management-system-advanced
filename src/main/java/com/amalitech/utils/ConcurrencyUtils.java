package com.amalitech.utils;

import com.amalitech.models.Account;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrencyUtils {

  public static void runSimulation(Account account) throws InterruptedException {
    runCustomSimulation(account, 100, 100, false);
  }

  public static void runCustomSimulation(
      Account account, int numDeposits, int numWithdrawals, boolean verbose)
      throws InterruptedException {
    int totalThreads = numDeposits + numWithdrawals;
    ExecutorService service =
        Executors.newFixedThreadPool(Math.min(totalThreads, 100)); // Cap thread pool size

    Runnable depositTask =
        () -> {
          try {
            double newBalance = account.deposit(10.0);
            if (verbose) {
              System.out.println(
                  Thread.currentThread().getName() + " deposited 10.0. New Balance: " + newBalance);
            }
          } catch (Exception e) {
            System.err.println("Deposit failed: " + e.getMessage());
          }
        };

    Runnable withdrawTask =
        () -> {
          try {
            double newBalance = account.withdraw(10.0);
            if (verbose) {
              System.out.println(
                  Thread.currentThread().getName() + " withdrew 10.0. New Balance: " + newBalance);
            }
          } catch (Exception e) {
            System.err.println("Withdrawal failed: " + e.getMessage());
          }
        };

    for (int i = 0; i < numDeposits; i++) {
      service.submit(depositTask);
    }
    for (int i = 0; i < numWithdrawals; i++) {
      service.submit(withdrawTask);
    }

    service.shutdown();
    service.awaitTermination(1, TimeUnit.MINUTES);
  }
}
