package quellatalo.nin.fx.propdisplay;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ValueDisplay<T> extends PropertyDisplay<T> {
    @FXML
    private Label lblValue;

    public ValueDisplay() {
        lblValue = new Label();
        getChildren().add(lblValue);
    }

    @Override
    public void setValue(T value) {
        lblValue.setText(value.toString());
    }

    @Override
    public String getText() {
        return lblValue.getText();
    }
}
