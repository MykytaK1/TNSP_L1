package com.lnu.rty.schema.utils;

import com.lnu.rty.schema.model.Case;
import com.lnu.rty.schema.model.CaseRelationship;
import com.lnu.rty.schema.model.CasesSchema;
import com.lnu.rty.schema.model.Lambda;
import com.lnu.rty.schema.model.diffeq.DiffEqData;
import com.lnu.rty.schema.model.diffeq.DiffEqParams;
import flanagan.integration.DerivnFunction;

import java.util.List;
import java.util.Map;

public class EquationUtils {
    public static List<RungeKuttaCustom.FourthOrderValue> calcStatePos(DiffEqData diffEqData, CasesSchema schema) {
        DiffEqParams diffEqParams = diffEqData.getDiffEqParams();
        int to = diffEqParams.getTo();
        int from = diffEqParams.getFrom();
        int step = diffEqParams.getStep();

        RungeKuttaCustom rungeKutta = new RungeKuttaCustom();
        rungeKutta.setFinalValueOfX(to);
        rungeKutta.setInitialValueOfX(from);
        rungeKutta.setStepSize(step);

        double[] initialPx = new double[schema.getCases().size()];
        initialPx[0] = 1;
        rungeKutta.setInitialValuesOfY(initialPx);

        return rungeKutta.fourthOrder(buildEquations(diffEqData, schema.getCases()));
    }

    public static DerivnFunction buildEquations(DiffEqData diffEqData, List<Case> cases) {
        Map<Lambda, Double> intensities = diffEqData.getIntensities();

        return new DerivnFunction() {
            @Override
            public double[] derivn(double t, double[] p) {
                return cases.stream().mapToDouble(cs -> {
                    List<CaseRelationship> children = cs.getChildren();
                    List<CaseRelationship> parents = cs.getParents();
                    int stateNumber = cs.getStateNumber();
                    double result = 0;

                    if (!parents.isEmpty()) {
                        double parentsFactor = parents.stream()
                                .mapToDouble(parent -> {
                                    Double intensity = intensities.get(getLambda(parent));
                                    return intensity * p[parent.getRelCase().getStateNumber()];
                                }).sum();
                        result += parentsFactor;
                    }

                    if (!children.isEmpty()) {
                        double childrenFactor = children.stream()
                                .mapToDouble(child -> intensities.get(getLambda(child)))
                                .sum() * p[stateNumber];
                        result -= childrenFactor;
                    }
                    return result;
                }).toArray();
            }
        };
    }

    private static Lambda getLambda(CaseRelationship relationship) {
        return Lambda.getByLabel("λ" + relationship.getNotationNumber());
    }

}
