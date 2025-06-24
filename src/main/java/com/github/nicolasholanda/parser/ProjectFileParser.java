package com.github.nicolasholanda.parser;

import java.io.FileInputStream;
import java.io.IOException;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.model.Project;

public class ProjectFileParser {

    public static Project parse(String yamlFile) {
        try {
            if (!isProjectFile(yamlFile)) {
                System.out.println("File is not a project file");
                System.exit(1);
            }

            Yaml yaml = new Yaml(new Constructor(Project.class, new LoaderOptions()));
            Project project = yaml.load(new FileInputStream(yamlFile));

            if (project == null) {
                System.out.println("Error: Project file is empty");
                System.exit(1);
            }

            if (project.getEntities() == null || project.getEntities().isEmpty()) {
                System.out.println("Error: Project file does not contain any entities");
                System.exit(1);
            }

            System.out.println("Entities Found: " + project.getEntities().size());
            for (Entity entity : project.getEntities()) {
                System.out.println(entity.getName());
            }

            return project;
        } catch (IOException e) {
            System.out.println("Error reading YAML file: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private static boolean isProjectFile(String yamlFile) {
        return yamlFile.endsWith(".yaml") || yamlFile.endsWith(".yml");
    }
}
