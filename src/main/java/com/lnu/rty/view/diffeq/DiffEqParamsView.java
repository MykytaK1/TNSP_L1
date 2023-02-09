package com.lnu.rty.view.diffeq;

import com.lnu.rty.schema.model.Lambda;
import com.lnu.rty.schema.model.UnitsSchema;
import com.lnu.rty.schema.model.diffeq.DiffEqData;
import com.lnu.rty.schema.model.diffeq.DiffEqParams;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import lombok.Getter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class DiffEqParamsView extends VBox {
    @Getter
    private final ObjectProperty<DiffEqData> diffEqDataProperty = new SimpleObjectProperty<>();

    public DiffEqParamsView(UnitsSchema unitsSchema) {
        setSpacing(5);
        GridPane gridPaneL = new GridPane();
        GridPane gridPaneP = new GridPane();

        var defaultIntensities = UnitsSchema.getDefaultIntensities(unitsSchema);
        DiffEqData diffEqData = DiffEqData.instance(defaultIntensities, DiffEqParams.DEFAULT);
        diffEqDataProperty.set(diffEqData);

        getChildren().addAll(gridPaneP, gridPaneL);

        EqParams eqParams = new EqParams(diffEqData);
        List<Node[]> items = eqParams.getItemsL();
        for (int i = 0; i < items.size(); i++) {
            gridPaneL.addColumn(i, items.get(i));
        }

        List<Node[]> itemsP = eqParams.getItemsP();
        for (int i = 0; i < itemsP.size(); i++) {
            gridPaneP.addColumn(i, itemsP.get(i));
        }

        Button apply = new Button("Calculate");
        apply.setOnAction(action -> diffEqDataProperty.set(eqParams.getDiffEqData()));

        gridPaneP.addRow(gridPaneL.getRowCount() + 2, apply);

        gridPaneL.setHgap(7);
        gridPaneL.setVgap(1);
        gridPaneP.setHgap(7);
        gridPaneP.setVgap(1);
        this.setPadding(new Insets(7));
    }

    private static class EqParams {
        private final DiffEqData diffEqData;

        private final List<Node[]> nodesL = new ArrayList<>();
        private final List<Node[]> nodesP = new ArrayList<>();

        public EqParams(DiffEqData diffEqData) {
            this.diffEqData = diffEqData;
            Map<Lambda, Double> intensities = diffEqData.getIntensities();
            intensities.entrySet().stream().sorted((Comparator.comparing(i -> i.getKey().getLabel()))).forEach((k) -> {
                EqParam<Double> eqParam = new EqParam<>(k.getKey().getLabel(),
                        k.getValue(),
                        EqParam.DOUBLE_CONVERTER,
                        i -> diffEqData.getIntensities().put(k.getKey(), i));
                addItemL(eqParam);
            });

            addEqDataParams(diffEqData);
        }

        private void addEqDataParams(DiffEqData diffEqData) {
            DiffEqParams diffEqParams = diffEqData.getDiffEqParams();
            EqParam<Integer> eqParamFrom = new EqParam<>("From",
                    diffEqParams.getFrom(),
                    EqParam.INTEGER_CONVERTER,
                    diffEqData.getDiffEqParams()::setFrom);
            EqParam<Integer> eqParamTo = new EqParam<>("To",
                    diffEqParams.getTo(),
                    EqParam.INTEGER_CONVERTER,
                    diffEqData.getDiffEqParams()::setTo);
            EqParam<Integer> eqParamStep = new EqParam<>("Step",
                    diffEqParams.getStep(),
                    EqParam.INTEGER_CONVERTER,
                    diffEqData.getDiffEqParams()::setStep);

            addItemP(eqParamFrom);
            addItemP(eqParamTo);
            addItemP(eqParamStep);
        }

        private <T extends Number> void addItemL(EqParam<T> eqParam) {
            nodesL.add(new Node[]{eqParam.getKeyLabel(), eqParam.getValue()});
        }

        private <T extends Number> void addItemP(EqParam<T> eqParam) {
            nodesP.add(new Node[]{eqParam.getKeyLabel(), eqParam.getValue()});
        }

        public DiffEqData getDiffEqData() {
            return DiffEqData.instance(
                    diffEqData.getIntensities(),
                    diffEqData.getDiffEqParams()
            );
        }

        public List<Node[]> getItemsL() {
            return nodesL;
        }

        public List<Node[]> getItemsP() {
            return nodesP;
        }

    }

    private static class EqParam<T extends Number> {
        static final StringConverter<Integer> INTEGER_CONVERTER = new IntegerStringConverter();
        static final StringConverter<Double> DOUBLE_CONVERTER = new DoubleStringConverter() {
            @Override
            public String toString(Double value) {
                DecimalFormat df = new DecimalFormat("0");
                df.setMaximumFractionDigits(50);
                return df.format(value.doubleValue());
            }
        };
        @Getter
        private Label keyLabel;
        @Getter
        private TextField value;

        private EqParam(String key, T defaultValue, StringConverter<T> converter, Consumer<T> consumer) {
            keyLabel = new Label(key);
            value = new TextField();
            value.setAlignment(Pos.CENTER_RIGHT);
            value.setEditable(true);
            value.setPrefColumnCount(6);
            var textFormatter = new TextFormatter<>(converter, defaultValue);
            value.setTextFormatter(textFormatter);
            value.textProperty().addListener(
                    (observable, oldValue, newValue) ->
                            consumer.accept(converter.fromString(newValue))
            );
        }
    }
}
