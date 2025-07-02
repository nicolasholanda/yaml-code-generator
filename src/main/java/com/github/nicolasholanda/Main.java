package com.github.nicolasholanda;

import com.github.nicolasholanda.generator.ProjectGenerator;
import com.github.nicolasholanda.generator.EntityGenerator;
import com.github.nicolasholanda.generator.FieldGenerator;
import com.github.nicolasholanda.generator.RepositoryGenerator;
import com.github.nicolasholanda.generator.ServiceGenerator;
import com.github.nicolasholanda.generator.ControllerGenerator;
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
            ProjectFileParser parser = new ProjectFileParser();
            Project project = parser.parse(yamlFile);
            FieldGenerator fieldGenerator = new FieldGenerator();
            EntityGenerator entityGenerator = new EntityGenerator(fieldGenerator);
            RepositoryGenerator repositoryGenerator = new RepositoryGenerator();
            ServiceGenerator serviceGenerator = new ServiceGenerator();
            ControllerGenerator controllerGenerator = new ControllerGenerator();
            ProjectGenerator projectGenerator = new ProjectGenerator(entityGenerator, repositoryGenerator, serviceGenerator, controllerGenerator);
            projectGenerator.generate(project);
        } catch (Exception e) {
            System.out.println("Error while generating project: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}