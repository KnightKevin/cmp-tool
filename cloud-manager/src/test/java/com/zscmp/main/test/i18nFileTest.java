package com.zscmp.main.test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.zscmp.cloud.manager.JavaFileVisitor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class i18nFileTest {

    @Test
    public void i18nTest() throws Exception {
        Path startPath = Paths.get("D:\\work_space\\cmp-tool\\vm-server"); // 指定你要遍历的目录路径
        JavaFileVisitor visitor = new JavaFileVisitor();

        final String targetMethodCall = "CheckUtil#check";

        Files.walkFileTree(startPath, visitor);
        List<Path> javaFiles = visitor.getJavaFiles();

        for (Path javaFile : javaFiles) {

            String javaClassName = javaFile.getFileName().toString().replace(".java", "");

            if (!javaFile.toUri().toString().endsWith(".java")) {
                continue;
            }

            CompilationUnit cu = StaticJavaParser.parse(javaFile);

            cu.accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(MethodCallExpr method, Void arg) {
                    super.visit(method, arg);

                    Expression scope = method.getScope().orElse(null);
                    String methCall = String.format("%s#%s", scope==null?"":scope.toString(),method.getName().getIdentifier());
                    List<Expression> arguments = method.getArguments();
                    if (methCall.equalsIgnoreCase(targetMethodCall)) {

                        if (arguments.size() >= 3 && arguments.get(2).isStringLiteralExpr()) {

                            String v = arguments.get(2).asStringLiteralExpr().asString();
                            System.out.println(v);
                        }

                        Expression argExpr = method.getArguments().get(0);
                        List<String> dd = method.getArguments().stream().map(Node::toString).collect(Collectors.toList());
                    }

                }
            }, null);

        }

    }
}
