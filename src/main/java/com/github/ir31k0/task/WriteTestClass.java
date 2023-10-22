package com.github.ir31k0.task;

import com.github.ir31k0.conversion.TypeToDefaultValue;
import com.github.ir31k0.data.Global;
import com.github.ir31k0.data.TestClassInfo;
import com.github.ir31k0.helper.FileHelper;
import com.google.common.base.CaseFormat;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WriteTestClass {
    public static void main(final String[] args) {
        extendClass("src/test/java/NumberOfGoodPairs.java");
    }

    public static TestClassInfo write(String questionSlug, String codeSnippet) {
        String className = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, questionSlug);
        String classContent = Global.SOLUTION_TEMPLATE.replace("%CLASS_NAME%", className).replace("%SOLUTION_CONTENT%", codeSnippet);
        String testClassPath = "src/test/java/" + className + ".java";
        try (FileWriter fileWriter = new FileWriter(testClassPath)) {
            fileWriter.write(classContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extendClass(testClassPath);
    }

    public static TestClassInfo extendClass(String path) {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8)) {
            final Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(List.of(new File(path)));
            final JavacTask javacTask = (JavacTask) compiler.getTask(null, fileManager, null, null, null, compilationUnits);
            final Iterable<? extends CompilationUnitTree> compilationUnitTrees = javacTask.parse();
            final ClassTree classTree = (ClassTree) compilationUnitTrees.iterator().next().getTypeDecls().get(0);
            final List<? extends Tree> classMemberList = classTree.getMembers();
            final ClassTree solutionClassTree = classMemberList.stream()
                    .filter(ClassTree.class::isInstance)
                    .map(ClassTree.class::cast)
                    .findFirst().orElseThrow();
            MethodTree method = solutionClassTree.getMembers().stream()
                    .filter(MethodTree.class::isInstance)
                    .map(MethodTree.class::cast)
                    .findFirst().orElseThrow();
            String returnType = method.getReturnType().toString();
            String javaClass = FileHelper.read(path);
            Pattern p = Pattern.compile(".+ (\\w+)\\(.+\\) \\{\\R\\s*()\\R\\s*\\}", Pattern.MULTILINE);
            Matcher m = p.matcher(javaClass);
            if (!m.find()) {
                throw new RuntimeException("Regex pattern in WriteTestClass not found");
            }
            StringBuffer s = new StringBuffer();
            s.append(javaClass, 0, m.start(2));
            s.append("return ");
            s.append(TypeToDefaultValue.convert(returnType));
            s.append(";");
            s.append(javaClass, m.start(2), javaClass.length() - 1);
            FileHelper.write(path, s.toString());
            TestClassInfo info = new TestClassInfo();
            info.setPath(path);
            info.setMethodName(m.group(1));
            info.setParameters(method.getParameters().stream().map(param -> param.getType().toString()).toList());
            info.setReturnType(returnType);
            return info;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
