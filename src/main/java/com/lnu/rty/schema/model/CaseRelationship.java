package com.lnu.rty.schema.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor(staticName = "instance")
@ToString(of = {"relCase", "notationNumber"})
public class CaseRelationship {
    private final Case relCase;
    private final int notationNumber;
}
