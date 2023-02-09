package com.lnu.rty.schema.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
/*

λАЗ1=5∙10-4 год-1,
λАЗ2=4∙10-4 год-1,
λАЗ3=3∙10-4 год-1,
λАЗ4=2.5∙10-4 год-1,
λАЗ5=5∙10-4 год-1,
λПЗ1=5∙10-4 год-1,
λПЗ2=4∙10-4 год-1,
λПЗ3=3∙10-4 год-1,
λПЗ4=2∙10-4 год-1,
λПЗ5=1∙10-4 год-1
 */
public class UnitsSchema4th implements UnitsSchema {

    private static final String SCHEMA_PATH = "src/main/resources/com/lnu/rty/4/img.png";

    @Override
    public Path schemaView() {
        return Path.of(SCHEMA_PATH);
    }
    private static boolean isEnabled(Map<Lambda, Unit> units, Lambda name) {
        return units.get(name).isEnabled();
    }

    @Override
    public Boolean apply(List<Unit> unitsList) {
        var units = UnitsSchema.toMap(unitsList);
        return ((isEnabled(units, Lambda.L1) && isEnabled(units, Lambda.L2))
                ||
                (isEnabled(units, Lambda.L3)))
                &&
                (isEnabled(units, Lambda.L4) || isEnabled(units, Lambda.L5));
    }

    @Override
    public List<Unit> basicUnits() {
        return List.of(
                Unit.instance("АЗ_1/ПЗ_1", Lambda.L1),
                Unit.instance("АЗ_2", Lambda.L2),
                Unit.instance("АЗ_3/ПЗ_3", Lambda.L3),
                Unit.instance("АЗ_4", Lambda.L4),
                Unit.instance("АЗ_5", Lambda.L5));
    }



    @Override
    public String equation() {
        return "((λ1 && λ2) || λ3) && (λ4 || λ5)";
    }
}
