package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.model.EntityField;
import com.github.nicolasholanda.model.Project;
import com.github.nicolasholanda.util.PackageConstants;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectGeneratorTest {
    private static final String MODEL_PATH = "output/src/main/java/com/example/" + PackageConstants.MODEL;
    private static final String REPOSITORY_PATH = "output/src/main/java/com/example/" + PackageConstants.REPOSITORY;

    @Test
    void testGenerateProjectWithMultipleEntities() throws Exception {
        EntityField idField = new EntityField();
        idField.setName("id");
        idField.setType("Long");
        EntityField nameField = new EntityField();
        nameField.setName("name");
        nameField.setType("String");
        Entity user = new Entity();
        user.setName("User");
        user.setFields(List.of(idField));
        user.setPackageName("com.example");
        user.setAccessModifier("public");
        Entity product = new Entity();
        product.setName("Product");
        product.setFields(List.of(nameField));
        product.setPackageName("com.example");
        product.setAccessModifier("public");
        Project project = new Project();
        List<Entity> entities = new ArrayList<>();
        entities.add(user);
        entities.add(product);
        project.setEntities(entities);
        FieldGenerator fieldGenerator = new FieldGenerator();
        EntityGenerator entityGenerator = new EntityGenerator(fieldGenerator);
        RepositoryGenerator repositoryGenerator = new RepositoryGenerator();
        ProjectGenerator projectGenerator = new ProjectGenerator(entityGenerator, repositoryGenerator);
        projectGenerator.generate(project);
        Path userEntityFile = Path.of(MODEL_PATH, "User.java");
        Path productEntityFile = Path.of(MODEL_PATH, "Product.java");
        Path userRepositoryFile = Path.of(REPOSITORY_PATH, "UserRepository.java");
        Path productRepositoryFile = Path.of(REPOSITORY_PATH, "ProductRepository.java");
        assertTrue(Files.exists(userEntityFile));
        assertTrue(Files.exists(productEntityFile));
        assertTrue(Files.exists(userRepositoryFile));
        assertTrue(Files.exists(productRepositoryFile));
    }

    @Test
    void testGenerateProjectWithNoEntitiesDoesNothing() {
        Project project = new Project();
        project.setEntities(new ArrayList<>());
        FieldGenerator fieldGenerator = new FieldGenerator();
        EntityGenerator entityGenerator = new EntityGenerator(fieldGenerator);
        RepositoryGenerator repositoryGenerator = new RepositoryGenerator();
        ProjectGenerator projectGenerator = new ProjectGenerator(entityGenerator, repositoryGenerator);
        assertDoesNotThrow(() -> projectGenerator.generate(project));
    }

    @Test
    void testEntityGeneratorThrowsExceptionIsPropagated() {
        Entity entity = new Entity();
        entity.setName("User");
        entity.setFields(List.of());
        entity.setPackageName("com.example");
        entity.setAccessModifier("public");
        Project project = new Project();
        project.setEntities(List.of(entity));
        EntityGenerator entityGenerator = new EntityGenerator(new FieldGenerator()) {
            @Override
            public void generate(Entity e) {
                throw new RuntimeException("Simulated error");
            }
        };
        RepositoryGenerator repositoryGenerator = new RepositoryGenerator();
        ProjectGenerator projectGenerator = new ProjectGenerator(entityGenerator, repositoryGenerator);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectGenerator.generate(project));
        assertTrue(exception.getMessage().contains("Simulated error"));
    }
} 