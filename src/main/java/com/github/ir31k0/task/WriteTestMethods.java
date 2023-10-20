package com.github.ir31k0.task;

import com.github.ir31k0.data.TestClassInfo;
import com.github.ir31k0.conversion.TestcaseToInitialization;
import com.github.ir31k0.helper.FileHelper;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WriteTestMethods {
    public static void write(TestClassInfo info, List<String> testcases, List<String> expectedAnswers) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < testcases.size(); i++) {
            builder
                    .append("\n@Test\npublic void test")
                    .append(i + 1)
                    .append("() {\nAssertions.assertEquals(")
                    .append(expectedAnswers.get(i))
                    .append(", new Solution().")
                    .append(info.getMethodName())
                    .append("(")
                    .append(parameters(testcases.get(i), info.getParameters()))
                    .append("));\n}\n");
        }

        String testClassContent = Files.asCharSource(new File(info.getPath()), StandardCharsets.UTF_8).read();
        String newTestClassContent = testClassContent.substring(0, testClassContent.indexOf("//BEGIN_TESTS") + 13) + // "//BEGIN_TESTS".length = 13
                builder +
                testClassContent.substring(testClassContent.indexOf("//END_TESTS"));
        FileHelper.write(info.getPath(), newTestClassContent);
    }

    private static String parameters(String testcases, List<String> parameterTypes) {
        List<String> parameters = new ArrayList<>();
        String[] splitTestcase = testcases.split("\\R");
        for (int i = 0; i < parameterTypes.size(); i++) {
            parameters.add(TestcaseToInitialization.convert(splitTestcase[i], parameterTypes.get(i)));
        }
        return String.join(", ", parameters);
    }

    private static List<String> splitTestcases(String testcases) {
        List<String> splitTestcases = new ArrayList<>();
        char[] testcasesArray = testcases.toCharArray();
        boolean arrayOpen = false;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < testcasesArray.length; i++) {
            char c = testcasesArray[i];
            switch (c) {
                case '[':
                    arrayOpen = true;
                    builder.append(c);
                    break;
                case ']':
                    arrayOpen = false;
                    builder.append(c);
                    break;
                case ',':
                    if (arrayOpen) {
                        builder.append(c);
                    } else {
                        splitTestcases.add(builder.toString());
                        builder = new StringBuilder();
                    }
                    break;
                default:
                    builder.append(c);
                    break;
            }
        }
        return splitTestcases;
    }
}
