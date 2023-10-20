package com.github.ir31k0.data;

import lombok.Data;

import java.util.List;

@Data
public class TestClassInfo {
    private String path;
    private String methodName;
    private List<String> parameters;
    private String returnType;
}
