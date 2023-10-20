package com.github.ir31k0.task;

import com.github.ir31k0.helper.RequestHelper;
import com.github.ir31k0.data.Global;
import com.github.ir31k0.response.interpretsolution.CheckResponse;
import lombok.Builder;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeRunner {
    /* %s for the question slug */
    private static final String RUN_CODE_URL_TEMPLATE = "https://leetcode.com/problems/%s/interpret_solution/";
    /* %s for the interpret id */
    private static final String CHECK_URL_TEMPLATE = "https://leetcode.com/submissions/detail/%s/check/";

    // CHECK_MAX_TRIES (12) * CHECK_WAIT_TIME_IN_SECONDS (5) = max wait time (1 minute)
    private static final int CHECK_MAX_TRIES = 12;
    private static final int CHECK_WAIT_TIME_IN_SECONDS = 5;

    public static CheckResponse runCode(Request request) throws IOException {
        // TODO Move everything into a try catch and remove throws
        String url = getRunCodeUrl(request.questionSlug);
        HttpPost post = new HttpPost(url);
        post.setEntity(createEntity(request));
        RequestHelper.applyDefaultHeaders(post, request.questionSlug, request.sessionToken, request.csrfToken);

        String textResponseBody = Global.CLIENT.execute(post, response -> {
            String responseBody = EntityUtils.toString(response.getEntity());
            if (response.getCode() == 200) {
                return responseBody;
            }
            throw new RuntimeException(String.format("Called '%s'; Got status code: '%s'; Message: '%s'", url, response.getCode(), responseBody));
        });
        Map<String, String> map = Global.GSON.fromJson(textResponseBody, Map.class);
        CheckResponse checkResponse = waitForCheckCompleted(map.get("interpret_id"), request);
        if (checkResponse == null) {
            // TODO Handle via exception?
            throw new RuntimeException(String.format("No result after max wait time of %ss - maybe the servers are currently busy?", CHECK_MAX_TRIES * CHECK_WAIT_TIME_IN_SECONDS));
        }
        return checkResponse;
    }

    private static HttpEntity createEntity(Request request) {
        Map<String, Object> content = new HashMap<>();
        content.put("lang", "java");
        content.put("question_id", request.questionId);
        content.put("typed_code", request.solution);
        content.put("data_input", String.join("\n", request.testcases));
        return new StringEntity(Global.GSON.toJson(content));
    }

    private static CheckResponse waitForCheckCompleted(String interpretId, Request request) throws IOException {
        // TODO Move everything into a try catch and remove throws
        System.out.print("Wait for complete..");
        int currentTry = 0;
        while (currentTry++ < CHECK_MAX_TRIES) {
            String url = getCheckUrl(interpretId);
            HttpGet get = new HttpGet(url);
            get.addHeader("Content-Type", "application/json");
            get.addHeader("Accept", "*/*");
            get.addHeader("User-Agent", Global.USER_AGENT);
            get.addHeader("Cookie", "csrftoken=" + request.csrfToken + "; LEETCODE_SESSION=" + request.sessionToken);
            // TODO Refactor urls -> move into Global class
            get.addHeader("Referer", "https://leetcode.com/problems/" + request.questionSlug + "/description/");
            get.addHeader("Origin", "https://leetcode.com");
            get.addHeader("X-Csrftoken", request.csrfToken);
            String textResponseBody = Global.CLIENT.execute(get, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                if (response.getCode() == 200) {
                    return responseBody;
                }
                throw new RuntimeException(String.format("Called '%s'; Got status code: '%s'; Message: '%s'", url, response.getCode(), responseBody));
            });
            Map<String, Object> map = Global.GSON.fromJson(textResponseBody, Map.class);
            String state = (String) map.get("state");
            // TODO Refactor
            if ("PENDING".equals(state) || "STARTED".equals(state)) {
                System.out.print("...");
            } else if ("SUCCESS".equals(state)) {
                System.out.println("SUCCESS");
                return Global.GSON.fromJson(textResponseBody, CheckResponse.class);
            } else {
                throw new RuntimeException(String.format("Unknown state '%s' while checking for the run result of %s. Response body: %s", state, url, textResponseBody));
            }
            try {
                Thread.sleep(CHECK_WAIT_TIME_IN_SECONDS * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private static String getRunCodeUrl(String questionSlug) {
        return String.format(RUN_CODE_URL_TEMPLATE, questionSlug);
    }

    private static String getCheckUrl(String interpretId) {
        // https://leetcode.com/submissions/detail/runcode_1696716803.0852768_mbT7wmdWhK/check/
        return String.format(CHECK_URL_TEMPLATE, interpretId);
    }

    @Builder
    public static class Request {
        private String questionId;
        private String questionSlug;
        private String solution;
        private Iterable<String> testcases;
        private String csrfToken;
        private String sessionToken;
    }
}
