package com.lnu.rty.view.diffeq;

import com.lnu.rty.schema.model.Case;
import com.lnu.rty.schema.model.CasesSchema;
import com.lnu.rty.schema.model.diffeq.DiffEqData;
import com.lnu.rty.schema.model.diffeq.relres.CaseDiffEqRes;
import com.lnu.rty.schema.model.diffeq.relres.CaseProbability;
import com.lnu.rty.schema.utils.EquationUtils;
import com.lnu.rty.schema.utils.RungeKuttaCustom;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.tableview2.TableColumn2;
import org.controlsfx.control.tableview2.TableView2;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DiffEqView extends Pane {

    private final ObjectProperty<List<CaseDiffEqRes>> results = new SimpleObjectProperty<>();
    private final CasesSchema schema;

    public DiffEqView(ObjectProperty<DiffEqData> diffEqDataProperty, CasesSchema schema) {
        this.schema = schema;
        TableView2<CaseDiffEqRes> table = buildTable(schema);
        table.prefWidthProperty().bind(widthProperty());
        table.prefHeightProperty().bind(heightProperty());
        getChildren().add(new ScrollPane(table));

        diffEqDataProperty.addListener((observable, oldValue, newValue) -> {
            List<CaseDiffEqRes> diffEqRes = results(newValue);
            results.set(diffEqRes);
        });

    }

    private static TableView2<CaseDiffEqRes> getResultsTable(CasesSchema schema) {
        TableView2<CaseDiffEqRes> resultsTable = new TableView2<>();

        TableColumn2<CaseDiffEqRes, Integer> n = new TableColumn2<>("n");
        n.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getTableView().itemsProperty().get().indexOf(param.getValue())));
        resultsTable.getColumns().add(n);

        TableColumn2<CaseDiffEqRes, Integer> t = new TableColumn2<>("t");
        t.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getFactor()));
        t.setMinWidth(60);
        resultsTable.getColumns().add(t);

        TableColumn2<CaseDiffEqRes, String> sum = new TableColumn2<>("Sum");
        sum.setCellValueFactory(param -> doubleCellValue(param.getValue().getSum()));
        sum.setPrefWidth(60);
        resultsTable.getColumns().add(sum);

        TableColumn2<CaseDiffEqRes, String> pt = new TableColumn2<>("p(t)");
        pt.setCellValueFactory(param -> doubleCellValue(param.getValue().getProbP()));
        pt.setPrefWidth(120);
        resultsTable.getColumns().add(pt);

        TableColumn2<CaseDiffEqRes, String> qt = new TableColumn2<>("q(t)");
        qt.setCellValueFactory(param -> doubleCellValue(param.getValue().getProbQ()));
        qt.setPrefWidth(120);
        resultsTable.getColumns().add(qt);


        List<Case> schemaCases = new ArrayList<>(schema.getCases());
        schemaCases.sort(Comparator.comparing(Case::getStateNumber));
        schemaCases.forEach(c -> {
            TableColumn2<CaseDiffEqRes, String> column = new TableColumn2<>(c.getLabel());
            column.setCellValueFactory(param -> {
                double probability = param.getValue().getCasesRes().get(c.getStateNumber()).getProbability();
                return doubleCellValue(probability);
            });
            column.setPrefWidth(120);
            resultsTable.getColumns().add(column);
        });
        return resultsTable;
    }

    private static SimpleObjectProperty<String> doubleCellValue(double probability) {
        DecimalFormat df = new DecimalFormat("0.0");
        df.setMaximumFractionDigits(9);
        return new SimpleObjectProperty<>(df.format(probability));
    }

    private TableView2<CaseDiffEqRes> buildTable(CasesSchema schema) {
        TableView2<CaseDiffEqRes> resultsTable = getResultsTable(schema);
        results.addListener((observable, oldValue, newValue) -> {
            resultsTable.getItems().clear();
            resultsTable.getItems().addAll(newValue);
        });
        return resultsTable;
    }

    private List<CaseDiffEqRes> results(DiffEqData newValue) {
        List<CaseDiffEqRes> result = new ArrayList<>();

        List<Case> cases = schema.getCases();
        List<RungeKuttaCustom.FourthOrderValue> statePos = EquationUtils.calcStatePos(newValue, schema);
        for (RungeKuttaCustom.FourthOrderValue statePo : statePos) {
            CaseDiffEqRes caseDiffEqRes = new CaseDiffEqRes();
            result.add(caseDiffEqRes);
            for (int j = 0; j < statePo.getValues().length; j++) {
                caseDiffEqRes.getCasesRes().add(CaseProbability.instance(cases.get(j), statePo.getValues()[j]));
            }
            List<CaseProbability> casesRes = caseDiffEqRes.getCasesRes();
            double sum = 0;
            double pSum = 0;
            double qSum = 0;
            for (CaseProbability cp : casesRes) {
                double probability = cp.getProbability();
                sum += probability;
                if (cp.getPrCase().isValue()) {
                    pSum += probability;
                } else {
                    qSum += probability;
                }
            }
            caseDiffEqRes.setSum(sum);
            caseDiffEqRes.setProbP(pSum);
            caseDiffEqRes.setProbQ(qSum);
            caseDiffEqRes.setFactor(statePo.getFactor());
        }
        return result;
    }
}
