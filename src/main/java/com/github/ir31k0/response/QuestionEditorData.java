package com.github.ir31k0.response;

import java.util.ArrayList;

public class QuestionEditorData {
    public Data data;
    public static class CodeSnippet{
        public String lang;
        public String langSlug;
        public String code;
    }

    public static class Data{
        public Question question;
    }

    public static class Question{
        public String questionId;
        public String questionFrontendId;
        public ArrayList<CodeSnippet> codeSnippets;
        public String envInfo;
        public boolean enableRunCode;
    }
}
