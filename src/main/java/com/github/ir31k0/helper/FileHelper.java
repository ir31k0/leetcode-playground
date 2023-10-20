package com.github.ir31k0.helper;

import com.google.common.io.Files;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class FileHelper {
    @SneakyThrows
    public static void write(String path, String content) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        }
    }

    @SneakyThrows
    public static String read(String path) {
        return Files.asCharSource(new File(path), StandardCharsets.UTF_8).read();
    }
}
