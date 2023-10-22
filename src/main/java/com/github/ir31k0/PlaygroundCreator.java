package com.github.ir31k0;

import com.github.ir31k0.data.Global;
import com.github.ir31k0.data.TestClassInfo;
import com.github.ir31k0.helper.RequestHelper;
import com.github.ir31k0.query.ConsolePanelConfigQuery;
import com.github.ir31k0.query.QuestionEditorDataQuery;
import com.github.ir31k0.response.QuestionEditorData;
import com.github.ir31k0.response.ConsolePanelConfig;
import com.github.ir31k0.response.CheckResponse;
import com.github.ir31k0.task.CodeRunner;
import com.github.ir31k0.task.SolutionExtractor;
import com.github.ir31k0.task.WriteTestClass;
import com.github.ir31k0.task.WriteTestMethods;

import java.io.IOException;

public class PlaygroundCreator {
    private static final String QUESTION_SLUG = "number-of-good-pairs";

    public static void main(String[] args) throws IOException {
        String csrfToken = RequestHelper.requestCsrfToken(QUESTION_SLUG);
        QuestionEditorData questionEditorData = QuestionEditorDataQuery.download(QUESTION_SLUG, csrfToken);
        String javaCodeSnippet = questionEditorData.getData().getQuestion().getCodeSnippets().stream().filter(s -> s.getLangSlug().equals("java")).findFirst().orElseThrow().getCode();
        TestClassInfo info = WriteTestClass.write(QUESTION_SLUG, javaCodeSnippet);
        String solutionCode = SolutionExtractor.extract(info.getPath());
        ConsolePanelConfig consolePanelConfig = ConsolePanelConfigQuery.execute(QUESTION_SLUG, csrfToken);
        CheckResponse checkResponse = CodeRunner.runCode(CodeRunner.Request.builder()
                .questionId(questionEditorData.getData().getQuestion().getQuestionId())
                .questionSlug(QUESTION_SLUG)
                .solution(solutionCode)
                .testcases(consolePanelConfig.data.question.exampleTestcaseList)
                .csrfToken(csrfToken)
                .sessionToken(Global.SESSION_TOKEN)
                .build());
        WriteTestMethods.write(info, consolePanelConfig.data.question.exampleTestcaseList, checkResponse.expected_code_answer);
    }
}
