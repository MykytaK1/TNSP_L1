package com.lnu.rty.schema.utils;

import com.lnu.rty.schema.model.Case;
import com.lnu.rty.schema.model.CaseRelationship;
import com.lnu.rty.schema.model.CasesSchema;
import com.lnu.rty.schema.model.UnitsSchema;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SchemaUtils {

    public static final Comparator<Case> CASE_COMPARATOR = Comparator.comparing(Case::getStateNumber);

    public static CasesSchema buildTree(UnitsSchema unitsSchema) {
        var count = new AtomicInteger();
        Map<String, Case> cases = new HashMap<>();
        var units = unitsSchema.basicUnits();

        Case root = new Case(units, true, UnitsSchema.notation(units));
        cases.put(UnitsSchema.stringNotation(root.getNotation()), root);

        extracted(unitsSchema, count, cases, root);
        return CasesSchema.instance(unitsSchema,
                root,
                cases.values().stream().sorted(CASE_COMPARATOR).toList());
    }

    private static void extracted(UnitsSchema unitsSchema, AtomicInteger count, Map<String, Case> cases, Case parent) {
        Integer[] parentNotation = parent.getNotation();

        for (int i = 0; i < parentNotation.length; i++) {
            if (parentNotation[i].equals(1)) {
                var units = UnitsSchema.clone(parent.getUnits());
                units.get(i).disable();

                boolean active = unitsSchema.apply(units);
                Integer[] notation = UnitsSchema.notation(units);
                String stringNotation = UnitsSchema.stringNotation(notation);
                Case resultCase;
                if (cases.containsKey(stringNotation)) {
                    resultCase = cases.get(stringNotation);

                } else {
                    resultCase = new Case(units, active, notation);
                    resultCase.setStateNumber(count.incrementAndGet());
                    cases.put(stringNotation, resultCase);
                    if (active) {
                        extracted(unitsSchema, count, cases, resultCase);
                    }
                }
                int notationNumber = i + 1;
                parent.getChildren().add(CaseRelationship.instance(resultCase, notationNumber));
                resultCase.getParents().add(CaseRelationship.instance(parent, notationNumber));
            }
        }
    }
}

