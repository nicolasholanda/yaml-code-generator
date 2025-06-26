package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.EntityField;
import com.squareup.javapoet.FieldSpec;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class FieldGeneratorTest {
    @Test
    void testGenerateSimpleField() {
        EntityField field = new EntityField();
        field.setName("id");
        field.setType("Long");
        field.setAccessModifier("private");

        FieldGenerator generator = new FieldGenerator();
        FieldSpec fieldSpec = generator.generate(field);

        assertEquals("id", fieldSpec.name);
        assertEquals("java.lang.Long", fieldSpec.type.toString());
        assertTrue(fieldSpec.modifiers.contains(Modifier.PRIVATE));
    }

    @Test
    void testGenerateStringField() {
        EntityField field = new EntityField();
        field.setName("name");
        field.setType("String");
        field.setAccessModifier("private");

        FieldGenerator generator = new FieldGenerator();
        FieldSpec fieldSpec = generator.generate(field);

        assertEquals("name", fieldSpec.name);
        assertEquals("java.lang.String", fieldSpec.type.toString());
        assertTrue(fieldSpec.modifiers.contains(Modifier.PRIVATE));
    }

    @Test
    void testGenerateFullyQualifiedType() {
        EntityField field = new EntityField();
        field.setName("date");
        field.setType("java.time.LocalDate");
        field.setAccessModifier("private");

        FieldGenerator generator = new FieldGenerator();
        FieldSpec fieldSpec = generator.generate(field);

        assertEquals("date", fieldSpec.name);
        assertEquals("java.time.LocalDate", fieldSpec.type.toString());
        assertTrue(fieldSpec.modifiers.contains(Modifier.PRIVATE));
    }

    @Test
    void testGeneratePublicField() {
        EntityField field = new EntityField();
        field.setName("active");
        field.setType("Boolean");
        field.setAccessModifier("public");

        FieldGenerator generator = new FieldGenerator();
        FieldSpec fieldSpec = generator.generate(field);

        assertEquals("active", fieldSpec.name);
        assertEquals("java.lang.Boolean", fieldSpec.type.toString());
        assertTrue(fieldSpec.modifiers.contains(Modifier.PUBLIC));
    }

    @Test
    void testGenerateFieldWithNullAccessModifierDefaultsToPrivate() {
        EntityField field = new EntityField();
        field.setName("score");
        field.setType("Double");
        field.setAccessModifier(null);

        FieldGenerator generator = new FieldGenerator();
        FieldSpec fieldSpec = generator.generate(field);

        assertEquals("score", fieldSpec.name);
        assertEquals("java.lang.Double", fieldSpec.type.toString());
        assertTrue(fieldSpec.modifiers.contains(Modifier.PRIVATE));
    }

    @Test
    void testGenerateFieldWithInvalidTypeThrowsException() {
        EntityField field = new EntityField();
        field.setName("invalid");
        field.setType("NotAType");
        field.setAccessModifier("private");

        FieldGenerator generator = new FieldGenerator();
        Exception exception = assertThrows(RuntimeException.class, () -> generator.generate(field));
        assertTrue(exception.getMessage().contains("Class not found"));
    }

    @Test
    void testGenerateFieldWithInvalidNameThrowsException() {
        EntityField field = new EntityField();
        field.setName("1234amount");
        field.setType("Long");
        field.setAccessModifier("private");

        FieldGenerator generator = new FieldGenerator();
        Exception exception = assertThrows(RuntimeException.class, () -> generator.generate(field));
        assertTrue(exception.getMessage().contains("1234amount"));
    }
} 