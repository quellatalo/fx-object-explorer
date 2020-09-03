package io.github.quellatalo.fx.explr.propdisplay;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public abstract class PropertyDisplay extends HBox {
    private DoubleProperty labelPrefWidth;
    @FXML
    private Label lblName;

    public PropertyDisplay() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PropertyDisplay.class.getSimpleName() + ".fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        labelPrefWidth = new SimpleDoubleProperty(this, "LabelWidth", -1);
        labelPrefWidth.bindBidirectional(lblName.prefWidthProperty());
    }

    public abstract void setValue(Object value);

    public double getLabelPrefWidth() {
        return labelPrefWidth.get();
    }

    public void setLabelPrefWidth(double labelPrefWidth) {
        this.labelPrefWidth.set(labelPrefWidth);
    }

    public DoubleProperty labelPrefWidthProperty() {
        return labelPrefWidth;
    }

    public void setLabel(String label) {
        lblName.setText(label);
    }

    public abstract String getText();
}
