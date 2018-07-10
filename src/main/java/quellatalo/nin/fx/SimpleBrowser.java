package quellatalo.nin.fx;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class SimpleBrowser<T> extends Stage {
    private ObjectExplorer<T> objectExplorer;

    public SimpleBrowser(T object) {
        objectExplorer = new ObjectExplorer<>(object);
        objectExplorer.setRoot(object);
        objectExplorer.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F3) {
                objectExplorer.getTvChildren().openFilter();
            }
        });
        setScene(new Scene(objectExplorer));
    }

    public T getObject() {
        return objectExplorer.getRoot();
    }

    public void setObject(T object) {
        objectExplorer.setRoot(object);
    }

    public ObjectExplorer<T> getObjectExplorer() {
        return objectExplorer;
    }
}
