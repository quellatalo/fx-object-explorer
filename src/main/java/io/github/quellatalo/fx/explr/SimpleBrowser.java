package io.github.quellatalo.fx.explr;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class SimpleBrowser<T> extends Stage {
    private ObjExplorer<T> objExplorer;

    public SimpleBrowser(T object) {
        objExplorer = new ObjExplorer<>(object);
        objExplorer.setRoot(object);
        Scene scene = new Scene(objExplorer);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F3) {
                objExplorer.getTvChildren().openFilter();
            }
        });
        setScene(scene);
    }

    public T getObject() {
        return objExplorer.getRoot();
    }

    public void setObject(T object) {
        objExplorer.setRoot(object);
    }

    public ObjExplorer<T> getObjExplorer() {
        return objExplorer;
    }
}
