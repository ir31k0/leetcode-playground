package com.github.ir31k0.helper;

import com.github.ir31k0.data.Global;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHttpRequest;

import java.io.IOException;

public class RequestHelper {
    public static void applyDefaultHeaders(BasicHttpRequest request, String questionSlug, String sessionToken) {
        applyDefaultHeaders(request, questionSlug, sessionToken, null);
    }

    public static void applyDefaultHeaders(BasicHttpRequest request, String questionSlug, String sessionToken, String csrfToken) {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "*/*");
        request.addHeader("User-Agent", Global.USER_AGENT);
        StringBuilder cookie = new StringBuilder("LEETCODE_SESSION=");
        cookie.append(sessionToken);
        if (csrfToken != null) {
            cookie.append("; csrftoken=");
            cookie.append(csrfToken);
        }
        request.addHeader("Cookie", cookie.toString());
        request.addHeader("Referer", "https://leetcode.com/problems/" + questionSlug + "/description/");
        request.addHeader("Origin", "https://leetcode.com");
        if (csrfToken != null) {
            request.addHeader("X-Csrftoken", csrfToken);
        }
    }

    public static String requestCsrfToken(String questionSlug) throws IOException {
        HttpGet request = new HttpGet("https://leetcode.com/problems/" + questionSlug + "/description/");
        request.addHeader("Accept", "*/*");
        request.addHeader("User-Agent", Global.USER_AGENT);
        return Global.CLIENT.execute(request, response -> {
            if (response.getCode() != 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println(responseBody);
            }
            return response.getHeader("set-cookie").getValue().split(";")[0].split("=")[1];
        });
    }
}
