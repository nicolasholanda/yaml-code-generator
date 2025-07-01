package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.model.Project;

/**
 * ProjectGenerator is a class that generates a project from a yml file.
 */
public class ProjectGenerator {
    private final EntityGenerator entityGenerator;
    private final RepositoryGenerator repositoryGenerator;
    private final ServiceGenerator serviceGenerator;

    public ProjectGenerator(EntityGenerator entityGenerator, RepositoryGenerator repositoryGenerator, ServiceGenerator serviceGenerator) {
        this.entityGenerator = entityGenerator;
        this.repositoryGenerator = repositoryGenerator;
        this.serviceGenerator = serviceGenerator;
    }

    /**
     * Generate a project for the given project.
     * @param project The project to generate.
     */
    public void generate(Project project) {
        for (Entity entity : project.getEntities()) {
            entityGenerator.generate(entity);
            repositoryGenerator.generate(entity);
            serviceGenerator.generate(entity);
        }
    }
}
