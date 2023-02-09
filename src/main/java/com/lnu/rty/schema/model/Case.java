package com.lnu.rty.schema.model;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "notation")
@RequiredArgsConstructor
@ToString(of = {"stateNumber", "value", "parents"})
public class Case {
    private final List<Unit> units;
    private final boolean value;
    private final Integer[] notation;
    private int stateNumber;
    private List<CaseRelationship> parents = new ArrayList<>();
    private List<CaseRelationship> children = new ArrayList<>();

    public String getLabel() {
        return "P" + getStateNumber();
    }
}
