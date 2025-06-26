package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.model.EntityField;
import com.github.nicolasholanda.model.Project;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectGeneratorTest {
    private static final String OUTPUT_PATH = "output/src/main/java/com/example";

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
        ProjectGenerator projectGenerator = new ProjectGenerator(entityGenerator);
        projectGenerator.generate(project);
        Path userFile = Path.of(OUTPUT_PATH, "User.java");
        Path productFile = Path.of(OUTPUT_PATH, "Product.java");
        assertTrue(Files.exists(userFile));
        assertTrue(Files.exists(productFile));
    }

    @Test
    void testGenerateProjectWithNoEntitiesDoesNothing() {
        Project project = new Project();
        project.setEntities(new ArrayList<>());
        FieldGenerator fieldGenerator = new FieldGenerator();
        EntityGenerator entityGenerator = new EntityGenerator(fieldGenerator);
        ProjectGenerator projectGenerator = new ProjectGenerator(entityGenerator);
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
        ProjectGenerator projectGenerator = new ProjectGenerator(entityGenerator);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectGenerator.generate(project));
        assertTrue(exception.getMessage().contains("Simulated error"));
    }
} 