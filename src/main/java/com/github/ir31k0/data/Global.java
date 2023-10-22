package com.github.ir31k0.data;

import com.github.ir31k0.helper.FileHelper;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Global {
    public static final String GRAPHQL_URL = "https://leetcode.com/graphql";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36";

    public static final Gson GSON = new Gson();
    public static final HttpClient CLIENT = HttpClientBuilder.create().build();

    public static final Map<String, String> QUERIES = new HashMap<>();

    static {
        try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/"))) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".query"))
                    .forEach(path -> QUERIES.put(FileHelper.fileNameWithoutSuffix(path), FileHelper.read(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String SOLUTION_TEMPLATE = FileHelper.read("src/main/resources/solution.template");
    public static final String SESSION_TOKEN = FileHelper.read("session-token.txt");
}
