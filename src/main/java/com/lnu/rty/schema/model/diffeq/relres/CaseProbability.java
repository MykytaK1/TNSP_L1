package com.lnu.rty.schema.model.diffeq.relres;

import com.lnu.rty.schema.model.Case;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(staticName = "instance")
public class CaseProbability {
    private Case prCase;
    private double probability;
}
