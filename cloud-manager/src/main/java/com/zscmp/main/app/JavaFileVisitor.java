package com.zscmp.main.app;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class JavaFileVisitor extends SimpleFileVisitor<Path> {

    private List<Path> javaFiles = new ArrayList<>();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (file.toString().endsWith(".java")) {
            javaFiles.add(file);
        }
        return FileVisitResult.CONTINUE;
    }

    public List<Path> getJavaFiles() {
        return javaFiles;
    }

}
