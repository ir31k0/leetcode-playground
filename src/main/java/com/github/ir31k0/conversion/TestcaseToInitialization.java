package com.github.ir31k0.conversion;

public class TestcaseToInitialization {
    public static String convert(String testcase, String type) {
        switch (type) {
            case "int[]":
                return "new int[] { " + testcase.substring(1).substring(0, testcase.length() - 2) + " }";
            case "String":
                return testcase;
            default:
                throw new RuntimeException(String.format("The type '%s' is not known in the TestcaseToInitialization. Please add an implementation.", type));
        }
    }
}
