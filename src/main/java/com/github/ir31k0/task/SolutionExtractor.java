package com.github.ir31k0.task;

import com.github.ir31k0.helper.FileHelper;

public class SolutionExtractor {
    public static String extract(String testClassPath) {
        String testClassContent = FileHelper.read(testClassPath);
        testClassContent = testClassContent.substring(testClassContent.indexOf("//BEGIN_SOLUTION") + 16); // "//BEGIN_SOLUTION".length = 16
        testClassContent = testClassContent.substring(0, testClassContent.indexOf("//END_SOLUTION"));
        return testClassContent;
    }
}
