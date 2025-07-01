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

public class RepositoryGeneratorTest {
    private static final String OUTPUT_PATH = "output/src/main/java/com/example/" + PackageConstants.REPOSITORY;

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
    void testGenerateRepository() throws Exception {
        EntityField idField = new EntityField();
        idField.setName("id");
        idField.setType("Long");
        Entity entity = new Entity();
        entity.setName("User");
        entity.setFields(List.of(idField));
        entity.setPackageName("com.example");
        entity.setAccessModifier("public");

        RepositoryGenerator generator = new RepositoryGenerator();
        generator.generate(entity);

        Path repositoryFile = Path.of(OUTPUT_PATH, "UserRepository.java");
        assertTrue(Files.exists(repositoryFile));
        String content = Files.readString(repositoryFile);
        assertTrue(content.contains("public interface UserRepository"));
        assertTrue(content.contains("@Repository"));
        assertTrue(content.contains("JpaRepository<User, Long>"));
        assertTrue(content.contains("package com.example." + PackageConstants.REPOSITORY));
    }
} 