package com.example.demo.models;

import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

public class Languages {
    public static List<String> languages() {
        return Arrays.asList("C#", "Java", "PHP", "JavaScript", "Python");
    }
}
