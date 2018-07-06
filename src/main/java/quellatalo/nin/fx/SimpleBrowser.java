package quellatalo.nin.fx;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimpleBrowser extends Stage {
    private ObjectExplorer<Object> objectExplorer;

    public SimpleBrowser(Object object) {
        objectExplorer = new ObjectExplorer<>(object);
        objectExplorer.setRoot(object);
        setScene(new Scene(objectExplorer));
    }

    public Object getObject() {
        return objectExplorer.getRoot();
    }

    public void setObject(Object object) {
        objectExplorer.setRoot(object);
    }

    public ObjectExplorer<Object> getObjectExplorer() {
        return objectExplorer;
    }
}
