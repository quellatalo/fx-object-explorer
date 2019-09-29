package io.github.quellatalo.fx.explr.propdisplay;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class ObjectDisplay<T> extends PropertyDisplay<T> {
    @FXML
    private Hyperlink hplValue;
    private T value;
    private Consumer<ObjectDisplay<T>> actionConsumer;

    public ObjectDisplay() {
        hplValue = new Hyperlink();
        getChildren().add(hplValue);
        hplValue.setOnAction(event -> actionConsumer.accept(this));
    }

    @Override
    public String getText() {
        return hplValue.getText();
    }

    public void setText(String text) {
        hplValue.setText(text);
    }

    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
        if (value == null) {
            hplValue.setText("NULL");
        } else if (!(value instanceof Map || value instanceof Collection || value.getClass().isArray())) {
            hplValue.setText(value.toString());
        }
    }

    public Consumer<ObjectDisplay<T>> getActionConsumer() {
        return actionConsumer;
    }

    public void setActionConsumer(Consumer<ObjectDisplay<T>> actionConsumer) {
        this.actionConsumer = actionConsumer;
    }
}
