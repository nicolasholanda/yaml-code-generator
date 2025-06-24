package com.github.nicolasholanda.generator;

import java.io.File;

public class ProjectGenerator {

    public static void generate(File projectFile) {
        System.out.println("Generating project from " + projectFile.getAbsolutePath());
    }
}
