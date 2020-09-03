package io.github.quellatalo.fx.explr.propdisplay;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class ObjectDisplay extends PropertyDisplay {
    @FXML
    private Hyperlink hplValue;
    private Object value;
    private Class<?> typeArgument;
    private Consumer<ObjectDisplay> actionConsumer;

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

    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
        if (value == null) {
            hplValue.setText("NULL");
        } else if (!(value instanceof Map || value instanceof Collection || value.getClass().isArray())) {
            hplValue.setText(value.toString());
        }
    }

    public Class<?> getTypeArgument() {
        return typeArgument;
    }

    public void setTypeArgument(Class<?> typeArgument) {
        this.typeArgument = typeArgument;
    }

    public Consumer<ObjectDisplay> getActionConsumer() {
        return actionConsumer;
    }

    public void setActionConsumer(Consumer<ObjectDisplay> actionConsumer) {
        this.actionConsumer = actionConsumer;
    }
}
