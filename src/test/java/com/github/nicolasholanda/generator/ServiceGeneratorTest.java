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

public class ServiceGeneratorTest {
    private static final String OUTPUT_PATH = "output/src/main/java/com/example/" + PackageConstants.SERVICE;

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
    void testGenerateService() throws Exception {
        EntityField idField = new EntityField();
        idField.setName("id");
        idField.setType("Long");
        Entity entity = new Entity();
        entity.setName("User");
        entity.setFields(List.of(idField));
        entity.setPackageName("com.example");
        entity.setAccessModifier("public");

        ServiceGenerator generator = new ServiceGenerator();
        generator.generate(entity);

        Path serviceFile = Path.of(OUTPUT_PATH, "UserService.java");
        assertTrue(Files.exists(serviceFile));
        String content = Files.readString(serviceFile);
        assertTrue(content.contains("public class UserService"));
        assertTrue(content.contains("@Service"));
        assertTrue(content.contains("@Autowired"));
        assertTrue(content.contains("private UserRepository repository"));
        assertTrue(content.contains("package com.example." + PackageConstants.SERVICE));
    }
} 