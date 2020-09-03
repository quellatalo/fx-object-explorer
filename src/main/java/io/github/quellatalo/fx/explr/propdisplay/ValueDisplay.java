package io.github.quellatalo.fx.explr.propdisplay;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ValueDisplay extends PropertyDisplay {
    @FXML
    private Label lblValue;

    public ValueDisplay() {
        lblValue = new Label();
        getChildren().add(lblValue);
    }

    @Override
    public void setValue(Object value) {
        if (value != null)
            lblValue.setText(value.toString());
    }

    @Override
    public String getText() {
        return lblValue.getText();
    }
}
