package com.lnu.rty.schema.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = "name")
@RequiredArgsConstructor(staticName = "instance")
public class Unit {
    private final String name;
    private final Lambda label;
    @Setter
    private boolean enabled = true;

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    @Override
    protected Unit clone() {
        var unit = Unit.instance(this.getName(), this.getLabel());
        unit.setEnabled(this.isEnabled());
        return unit;
    }
}
