package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.model.Project;

/**
 * ProjectGenerator is a class that generates a project from a yml file.
 */
public class ProjectGenerator {

    /**
     * Generate a project for the given project.
     * @param project The project to generate.
     */
    public static void generate(Project project) {
        for (Entity entity : project.getEntities()) {
            EntityGenerator.generate(entity);
        }
    }
}
