package com.lnu.rty.schema.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(staticName = "instance")
public class CasesSchema {
    private final UnitsSchema unitsSchema;
    private final Case tree;
    private final List<Case> cases;
}
