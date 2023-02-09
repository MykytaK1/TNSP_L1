package com.lnu.rty.schema.model.diffeq.relres;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CaseDiffEqRes {
    private List<CaseProbability> casesRes = new ArrayList<>();
    private int factor;
    private double sum;
    private double probP;
    private double probQ;
}
