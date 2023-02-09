package com.lnu.rty.schema.model.diffeq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor(staticName = "instance")
public class DiffEqParams {

    public static final DiffEqParams DEFAULT = DiffEqParams.instance(0, 1000, 1);

    private int from;
    private int to;
    private int step;
}
