package com.github.ir31k0.response;

import lombok.Data;

import java.util.ArrayList;

@Data
public class QuestionEditorData {
    private Data data;

    @lombok.Data
    public static class CodeSnippet {
        private String lang;
        private String langSlug;
        private String code;
    }

    @lombok.Data
    public static class Data {
        private Question question;
    }

    @lombok.Data
    public static class Question {
        private String questionId;
        private String questionFrontendId;
        private ArrayList<CodeSnippet> codeSnippets;
        private String envInfo;
        private boolean enableRunCode;
    }
}
