package com.github.ir31k0.helper;

import com.google.common.io.Files;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class FileHelper {
    @SneakyThrows
    public static void write(String path, String content) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        }
    }

    @SneakyThrows
    public static String read(String path) {
        return read(new File(path));
    }

    @SneakyThrows
    public static String read(Path path) {
        return read(path.toFile());
    }

    @SneakyThrows
    public static String read(File file) {
        return Files.asCharSource(file, StandardCharsets.UTF_8).read();
    }

    public static String fileNameWithoutSuffix(Path path) {
        return path.getFileName().toString().split("\\.")[0];
    }
}
