package com.lnu.rty;

import com.lnu.rty.schema.model.CasesSchema;
import com.lnu.rty.schema.model.UnitsSchema;
import com.lnu.rty.schema.model.UnitsSchema4th;
import com.lnu.rty.schema.utils.SchemaUtils;
import com.lnu.rty.view.diffeq.CalcView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RtyApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        UnitsSchema unitsSchema = new UnitsSchema4th();
        CasesSchema schema = SchemaUtils.buildTree(unitsSchema);

        BorderPane calculationNode = new CalcView(schema);

        BorderPane root = new BorderPane();
        root.setCenter(calculationNode);
        calculationNode.prefHeightProperty().bind(root.heightProperty());
        calculationNode.prefWidthProperty().bind(root.widthProperty());
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

}