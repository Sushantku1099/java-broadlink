package com.broadlink.iot;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.*;

public class TestSuiteRunner {
    public static void main(String[] args) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(DiscoverySelectors.selectPackage("com.broadlink.iot"))
            .build();
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        System.out.println("\n=== TEST RESULTS ===");
        System.out.println("Tests found:    " + summary.getTestsFoundCount());
        System.out.println("Tests started:  " + summary.getTestsStartedCount());
        System.out.println("Tests succeeded:" + summary.getTestsSucceededCount());
        System.out.println("Tests failed:   " + summary.getTestsFailedCount());
        System.out.println("Tests skipped:  " + summary.getTestsSkippedCount());

        if (summary.getTestsFailedCount() > 0) {
            System.out.println("\n=== FAILURES ===");
            summary.getFailures().forEach(f -> {
                System.out.println("  " + f.getTestIdentifier().getDisplayName());
                System.out.println("    " + f.getException().getMessage());
            });
            System.exit(1);
        }
        System.out.println("\nALL TESTS PASSED");
    }
}
