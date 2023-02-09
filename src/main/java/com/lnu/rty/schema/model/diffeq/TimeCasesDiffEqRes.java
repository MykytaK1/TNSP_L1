package com.lnu.rty.schema.model.diffeq;

import com.lnu.rty.schema.model.diffeq.relres.CaseDiffEqRes;
import lombok.Data;
import org.apache.commons.collections4.map.LinkedMap;

@Data
public class TimeCasesDiffEqRes {
    private LinkedMap<Integer, CaseDiffEqRes> caseRes;
}
