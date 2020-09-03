package io.github.quellatalo.fx.explr;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class SimpleBrowser extends Stage {
    private ObjExplorer objExplorer;

    public SimpleBrowser(Object object, Class<?> type) {
        objExplorer = new ObjExplorer(object, type);
        Scene scene = new Scene(objExplorer);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F3) {
                objExplorer.getTvChildren().openFilter();
            }
        });
        setScene(scene);
    }

    public Object getObject() {
        return objExplorer.getRoot();
    }

    public ObjExplorer getObjExplorer() {
        return objExplorer;
    }
}
