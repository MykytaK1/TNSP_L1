package com.lnu.rty.view.diffeq;

import com.lnu.rty.schema.model.CasesSchema;
import com.lnu.rty.schema.model.diffeq.DiffEqData;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.BorderPane;

public class CalcView extends BorderPane {

    public CalcView(CasesSchema schema) {
        DiffEqParamsView diffEqParamsView = new DiffEqParamsView(schema.getUnitsSchema());
        ObjectProperty<DiffEqData> diffEqDataProperty = diffEqParamsView.getDiffEqDataProperty();

        DiffEqView results = new DiffEqView(diffEqDataProperty, schema);
        setCenter(results);
        setTop(diffEqParamsView);
    }
}
