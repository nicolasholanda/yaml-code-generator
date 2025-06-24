package com.github.nicolasholanda.model;

import java.util.List;
import lombok.Data;

@Data
public class Project {
    private String packageName;
    private List<Entity> entities;
}
