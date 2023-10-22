package com.github.ir31k0.query;

import com.github.ir31k0.data.Global;
import com.github.ir31k0.helper.RequestHelper;
import com.github.ir31k0.response.ConsolePanelConfig;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConsolePanelConfigQuery {
    public static ConsolePanelConfig execute(String questionSlug, String csrfToken) throws IOException {
        Map<String, Object> content = new HashMap<>();
        Map<String, String> variables = new HashMap<>();
        variables.put("titleSlug", questionSlug);

        String consolePanelConfig = Global.QUERIES.get("consolePanelConfig");
        if (consolePanelConfig == null) {
            throw new RuntimeException("consolePanelConfig query not found in queries map");
        }

        content.put("query", consolePanelConfig);
        content.put("variables", variables);

        HttpPost request = new HttpPost(Global.GRAPHQL_URL);
        request.setEntity(new StringEntity(Global.GSON.toJson(content)));
        RequestHelper.applyDefaultHeaders(request, questionSlug, Global.SESSION_TOKEN, csrfToken);
        return Global.CLIENT.execute(request, response -> {
            String responseBody = EntityUtils.toString(response.getEntity());
            if (response.getCode() == 200) {
                return Global.GSON.fromJson(responseBody, ConsolePanelConfig.class);
            }
            throw new RuntimeException(String.format("Called '%s'; Got status code: '%s'; Message: '%s'", Global.GRAPHQL_URL, response.getCode(), responseBody));
        });
    }
}
