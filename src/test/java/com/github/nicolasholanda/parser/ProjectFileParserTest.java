package com.github.nicolasholanda.parser;

import com.github.nicolasholanda.model.Project;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectFileParserTest {
    @Test
    void testParseSimpleYaml() throws Exception {
        String yaml = 
                    """
                    packageName: com.example.demo
                    entities:
                      - name: User
                        fields:
                          - name: id
                            type: Long
                          - name: name
                            type: String
                    """;
        File tempFile = File.createTempFile("test-project", ".yml");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(yaml);
        }

        ProjectFileParser parser = new ProjectFileParser();
        Project project = parser.parse(tempFile.getAbsolutePath());

        assertNotNull(project);
        assertEquals("com.example.demo", project.getPackageName());
        assertNotNull(project.getEntities());
        assertEquals(1, project.getEntities().size());
        assertEquals("User", project.getEntities().get(0).getName());
        assertEquals(2, project.getEntities().get(0).getFields().size());
    }

    @Test
    void testParseYamlWithMultipleEntities() throws Exception {
        String yaml = """
                    packageName: com.example.demo
                    entities:
                      - name: User
                        fields:
                          - name: id
                            type: Long
                      - name: Product
                        fields:
                          - name: code
                            type: String
                    """;
        File tempFile = File.createTempFile("test-multi-entities", ".yml");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(yaml);
        }
        ProjectFileParser parser = new ProjectFileParser();
        Project project = parser.parse(tempFile.getAbsolutePath());
        assertNotNull(project);
        assertEquals(2, project.getEntities().size());
        assertEquals("User", project.getEntities().get(0).getName());
        assertEquals("Product", project.getEntities().get(1).getName());
    }

    @Test
    void testParseYamlMissingPackageName() throws Exception {
        String yaml = """
                    entities:
                      - name: User
                        fields:
                          - name: id
                            type: Long
                    """;
        File tempFile = File.createTempFile("test-missing-package", ".yml");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(yaml);
        }
        ProjectFileParser parser = new ProjectFileParser();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(tempFile.getAbsolutePath()));
        assertTrue(exception.getMessage().contains("package name"));
    }

    @Test
    void testParseYamlMissingEntities() throws Exception {
        String yaml = """
                    packageName: com.example.demo
                    """;
        File tempFile = File.createTempFile("test-missing-entities", ".yml");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(yaml);
        }
        ProjectFileParser parser = new ProjectFileParser();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(tempFile.getAbsolutePath()));
        assertTrue(exception.getMessage().contains("entities"));
    }

    @Test
    void testParseYamlEntityMissingFields() throws Exception {
        String yaml = """
                    packageName: com.example.demo
                    entities:
                      - name: User
                    """;
        File tempFile = File.createTempFile("test-entity-missing-fields", ".yml");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(yaml);
        }
        ProjectFileParser parser = new ProjectFileParser();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(tempFile.getAbsolutePath()));
        assertTrue(exception.getMessage().contains("fields"));
    }

    @Test
    void testParseEmptyYaml() throws Exception {
        String yaml = "";
        File tempFile = File.createTempFile("test-empty-yaml", ".yml");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(yaml);
        }
        ProjectFileParser parser = new ProjectFileParser();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(tempFile.getAbsolutePath()));
        assertTrue(exception.getMessage().contains("empty"));
    }
} 