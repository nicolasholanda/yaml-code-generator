package com.github.nicolasholanda.model;

import java.util.List;

import lombok.Data;

@Data
public class Entity {
    private String name;
    private String accessModifier;
    private String packageName;
    private List<EntityField> fields;
}
