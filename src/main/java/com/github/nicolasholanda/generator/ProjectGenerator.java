package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.model.Project;

/**
 * ProjectGenerator is a class that generates a project from a yml file.
 */
public class ProjectGenerator {
    private final EntityGenerator entityGenerator;
    private final RepositoryGenerator repositoryGenerator;

    public ProjectGenerator(EntityGenerator entityGenerator, RepositoryGenerator repositoryGenerator) {
        this.entityGenerator = entityGenerator;
        this.repositoryGenerator = repositoryGenerator;
    }

    /**
     * Generate a project for the given project.
     * @param project The project to generate.
     */
    public void generate(Project project) {
        for (Entity entity : project.getEntities()) {
            entityGenerator.generate(entity);
            repositoryGenerator.generate(entity);
        }
    }
}
