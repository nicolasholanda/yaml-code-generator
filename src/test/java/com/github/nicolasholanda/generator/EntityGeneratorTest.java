package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.model.EntityField;
import com.github.nicolasholanda.util.PackageConstants;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityGeneratorTest {
    private static final String OUTPUT_PATH = "output/src/main/java/com/example/" + PackageConstants.MODEL;

    @BeforeEach
    void cleanOutputDir() throws Exception {
        Path dir = Path.of(OUTPUT_PATH);
        if (Files.exists(dir)) {
            Files.walk(dir)
                .map(Path::toFile)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(File::delete);
        }
        Files.createDirectories(dir);
    }

    @AfterEach
    void cleanUp() throws Exception {
        cleanOutputDir();
    }

    @Test
    void testGenerateSimpleEntity() throws Exception {
        EntityField idField = new EntityField();
        idField.setName("id");
        idField.setType("Long");
        Entity entity = new Entity();
        entity.setName("User");
        entity.setFields(List.of(idField));
        entity.setPackageName("com.example");
        entity.setAccessModifier("public");
        FieldGenerator fieldGenerator = new FieldGenerator();
        EntityGenerator generator = new EntityGenerator(fieldGenerator);
        generator.generate(entity);
        Path javaFile = Path.of(OUTPUT_PATH, "User.java");
        assertTrue(Files.exists(javaFile));
        String content = Files.readString(javaFile);
        assertTrue(content.contains("public class User"));
        assertTrue(content.contains("private Long id"));
    }

    @Test
    void testGenerateEntityWithMultipleFields() throws Exception {
        EntityField idField = new EntityField();
        idField.setName("id");
        idField.setType("Long");
        EntityField nameField = new EntityField();
        nameField.setName("name");
        nameField.setType("String");
        Entity entity = new Entity();
        entity.setName("Product");
        entity.setFields(List.of(idField, nameField));
        entity.setPackageName("com.example");
        entity.setAccessModifier("public");
        FieldGenerator fieldGenerator = new FieldGenerator();
        EntityGenerator generator = new EntityGenerator(fieldGenerator);
        generator.generate(entity);
        Path javaFile = Path.of(OUTPUT_PATH, "Product.java");
        assertTrue(Files.exists(javaFile));
        String content = Files.readString(javaFile);
        assertTrue(content.contains("private Long id"));
        assertTrue(content.contains("private String name"));
    }

    @Test
    void testGenerateEntityWithDifferentAccessModifier() throws Exception {
        EntityField idField = new EntityField();
        idField.setName("id");
        idField.setType("Long");
        Entity entity = new Entity();
        entity.setName("Admin");
        entity.setFields(List.of(idField));
        entity.setPackageName("com.example");
        entity.setAccessModifier("private");
        FieldGenerator fieldGenerator = new FieldGenerator();
        EntityGenerator generator = new EntityGenerator(fieldGenerator);
        generator.generate(entity);
        Path javaFile = Path.of(OUTPUT_PATH, "Admin.java");
        assertTrue(Files.exists(javaFile));
        String content = Files.readString(javaFile);
        assertTrue(content.contains("private class Admin"));
    }

    @Test
    void testGenerateEntityWithInvalidNameThrowsException() {
        EntityField idField = new EntityField();
        idField.setName("id");
        idField.setType("Long");
        Entity entity = new Entity();
        entity.setName("");
        entity.setFields(List.of(idField));
        entity.setPackageName("com.example");
        entity.setAccessModifier("public");
        FieldGenerator fieldGenerator = new FieldGenerator();
        EntityGenerator generator = new EntityGenerator(fieldGenerator);
        Exception exception = assertThrows(RuntimeException.class, () -> generator.generate(entity));
        assertTrue(exception.getMessage().toLowerCase().contains("name"));
    }
} 