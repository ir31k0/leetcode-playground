package com.github.ir31k0.response;

import java.util.ArrayList;

public class ConsolePanelConfig {
    public Data data;

    public static class Data{
        public Question question;
    }

    public static class Question{
        public String questionId;
        public String questionFrontendId;
        public String questionTitle;
        public boolean enableDebugger;
        public boolean enableRunCode;
        public boolean enableSubmit;
        public boolean enableTestMode;
        public ArrayList<String> exampleTestcaseList;
        public String metaData;
    }
}
