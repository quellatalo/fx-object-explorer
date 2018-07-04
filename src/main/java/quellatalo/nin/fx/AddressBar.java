package quellatalo.nin.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import quellatalo.nin.fx.propdisplay.ObjectDisplay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddressBar<T> extends HBox {
    private List<ObjectDisplay<T>> list;

    public AddressBar() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getClass().getSimpleName() + ".fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        list = new ArrayList<>();
    }

    public void setCurrent(ObjectDisplay<T> o) {
        ObjectDisplay<T> t = list.stream().filter(tObjectDisplay -> tObjectDisplay.getValue() == o.getValue()).findFirst().orElse(null);
        if (t == null) {
            list.add(o);
        } else {
            list.subList(list.indexOf(t) + 1, list.size()).clear();
        }
        generatePath();
    }

    public ObjectDisplay<T> getFist() {
        return list.get(0);
    }

    public ObjectDisplay<T> getPrevious() {
        return list.size() >= 2 ? list.get(list.size() - 2) : null;
    }

    private void generatePath() {
        getChildren().clear();
        for (ObjectDisplay<T> t : list) {
            ObjectDisplay<T> o = new ObjectDisplay<>();
            o.setLabel(" > ");
            o.setValue(t.getValue());
            o.setText(t.getText());
            o.setActionConsumer(t.getActionConsumer());
            getChildren().add(o);
        }
    }

    public void clear() {
        list.clear();
        getChildren().clear();
    }
}
