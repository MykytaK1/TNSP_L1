package com.lnu.rty.schema.model;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface UnitsSchema extends Function<List<Unit>, Boolean> {
    Map<String, Double> DEFAULT_INTENSITY = Map.of(
            "АЗ_1", 0.0005,
            "АЗ_2", 0.0004,
            "АЗ_3", 0.0003,
            "АЗ_4", 0.00025,
            "АЗ_5", 0.0005,
            "ПЗ_1", 0.0005,
            "ПЗ_2", 0.0004,
            "ПЗ_3", 0.0003,
            "ПЗ_4", 0.0002,
            "ПЗ_5", 0.0001
    );

    static Map<Lambda, Double> getDefaultIntensities(UnitsSchema unitsSchema) {
        List<Unit> units = unitsSchema.basicUnits();
        return units.stream().collect(Collectors.toMap(Unit::getLabel, v -> {
            String name = v.getName();
            String intensityLabel = name.split("/")[0];
            return DEFAULT_INTENSITY.get(intensityLabel);
        }));
    }

    static Map<Lambda, Unit> toMap(List<Unit> units) {
        return units.stream().collect(Collectors.toMap(Unit::getLabel, Function.identity()));
    }

    static Integer[] notation(List<Unit> units) {
        return units.stream().map(u -> u.isEnabled() ? 1 : 0).toArray(value -> new Integer[units.size()]);
    }

    static String stringNotation(Integer[] notation) {
        return Arrays.stream(notation).map(String::valueOf).collect(Collectors.joining(""));
    }

    static List<Unit> clone(List<Unit> units) {
        return units.stream().map(Unit::clone).collect(Collectors.toList());
    }

    static String[] basicIntensity() {
        return new String[]{
                "λАЗ_1=5∙10-4 год-1",
                "λАЗ_2=4∙10-4 год-1",
                "λАЗ_3=3∙10-4 год-1",
                "λАЗ_4=2.5∙10-4 год-1",
                "λАЗ_5=5∙10-4 год-1",
                "λПЗ_1=5∙10-4 год-1",
                "λПЗ_2=4∙10-4 год-1",
                "λПЗ_3=3∙10-4 год-1",
                "λПЗ_4=2∙10-4 год-1",
                "λПЗ_5=1∙10-4 год-1"
        };
    }

    List<Unit> basicUnits();

    Path schemaView();

    String equation();
}
