package com.lnu.rty.schema.model;

import lombok.Getter;

import java.util.Arrays;

public enum Lambda {
    L1("λ1"),
    L2("λ2"),
    L3("λ3"),
    L4("λ4"),
    L5("λ5"),
    L6("λ6"),
    L7("λ7");

    @Getter
    private final String label;

    Lambda(String label) {
        this.label = label;
    }

    public static Lambda getByLabel(String label) {
        return Arrays.stream(Lambda.values()).filter(l -> l.getLabel().equals(label)).findFirst().get();
    }
}
