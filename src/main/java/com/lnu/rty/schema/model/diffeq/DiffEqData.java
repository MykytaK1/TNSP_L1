package com.lnu.rty.schema.model.diffeq;

import com.lnu.rty.schema.model.Lambda;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@AllArgsConstructor(staticName = "instance")
public class DiffEqData {
    private Map<Lambda, Double> intensities;
    private DiffEqParams diffEqParams;
}
