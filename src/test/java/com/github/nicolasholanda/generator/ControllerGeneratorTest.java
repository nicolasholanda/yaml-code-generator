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

public class ControllerGeneratorTest {
    private static final String OUTPUT_PATH = "output/src/main/java/com/example/" + PackageConstants.CONTROLLER;

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
    void testGenerateController() throws Exception {
        EntityField idField = new EntityField();
        idField.setName("id");
        idField.setType("Long");
        Entity entity = new Entity();
        entity.setName("User");
        entity.setFields(List.of(idField));
        entity.setPackageName("com.example");
        entity.setAccessModifier("public");

        ControllerGenerator generator = new ControllerGenerator();
        generator.generate(entity);

        Path controllerFile = Path.of(OUTPUT_PATH, "UserController.java");
        assertTrue(Files.exists(controllerFile));
        String content = Files.readString(controllerFile);
        assertTrue(content.contains("public class UserController"));
        assertTrue(content.contains("@RestController"));
        assertTrue(content.contains("@RequestMapping"));
        assertTrue(content.contains("@Autowired"));
        assertTrue(content.contains("private UserService service"));
        assertTrue(content.contains("package com.example." + PackageConstants.CONTROLLER));
    }
} 