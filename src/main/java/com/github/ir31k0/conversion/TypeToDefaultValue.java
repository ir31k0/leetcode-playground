package com.github.ir31k0.conversion;

public class TypeToDefaultValue {
    public static String convert(String type) {
        switch (type) {
            case "int":
                return "0";
            case "boolean":
                return "false";
            default:
                throw new RuntimeException(String.format("The type '%s' is not known in the TypeToDefaultValue. Please add an implementation.", type));
        }
    }
}
