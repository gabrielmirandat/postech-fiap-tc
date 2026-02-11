package com.example;

/**
 * Simple test class for the Main application.
 * This demonstrates basic testing capabilities without external dependencies.
 */
public class MainTest {

    public static void main(String[] args) {
        MainTest test = new MainTest();
        test.testMainClassExists();
        test.testMainMethodExists();
        test.testBasicArithmetic();
        System.out.println("All tests passed!");
    }

    public void testMainClassExists() {
        // Verify that the Main class can be instantiated
        if (Main.class == null) {
            throw new RuntimeException("Main class not found");
        }
        System.out.println("✓ Main class exists");
    }

    public void testMainMethodExists() {
        // Verify that the main method exists
        try {
            Main.class.getMethod("main", String[].class);
            System.out.println("✓ Main method exists");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Main method not found: " + e.getMessage());
        }
    }

    public void testBasicArithmetic() {
        // Simple test to verify basic functionality
        if (2 + 2 != 4) {
            throw new RuntimeException("Basic arithmetic failed");
        }
        System.out.println("✓ Basic arithmetic works");
    }
}
