package com.github.nicolasholanda;

import com.github.nicolasholanda.generator.ProjectGenerator;
import com.github.nicolasholanda.model.Project;
import com.github.nicolasholanda.parser.ProjectFileParser;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar yaml-code-generator.jar <yaml-file>");
            System.exit(1);
        }

        try {
            String yamlFile = args[0];
            Project project = ProjectFileParser.parse(yamlFile);
            ProjectGenerator.generate(project);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}