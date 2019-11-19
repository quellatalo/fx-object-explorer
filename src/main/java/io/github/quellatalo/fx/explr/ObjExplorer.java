package io.github.quellatalo.fx.explr;

import io.github.quellatalo.fx.explr.propdisplay.PropertyDisplay;
import io.github.quellatalo.fx.explr.propdisplay.ValueDisplay;
import io.github.quellatalo.fx.tvx.TableViewX;
import io.github.quellatalo.fx.tvx.TitleStyle;
import io.github.quellatalo.reflection.ClassUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import io.github.quellatalo.fx.explr.propdisplay.ObjectDisplay;

public class ObjExplorer<T> extends GridPane {
    private static final Alert alert = new Alert(Alert.AlertType.INFORMATION, "NULL");
    @FXML
    private AddressBar<T> abPath;
    @FXML
    private ScrollPane propertiesPane;
    @FXML
    private VBox vbProperties;
    @FXML
    private TableViewX<T> tvChildren;
    @FXML
    private SplitPane splitPane;
    private T root;
    private ObjectDisplay<T> current;
    private ObjectProperty<TitleStyle> titleStyle;
    private BooleanProperty displayClass;
    private BooleanProperty displayHashCode;
    private DoubleProperty labelPrefWidth;
    private Consumer<ObjectDisplay<T>> setActiveItem;
    private Runnable onLoad;

    public ObjExplorer() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getClass().getSimpleName() + ".fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        titleStyle = tvChildren.titleStyleProperty();
        displayClass = tvChildren.displayClassProperty();
        displayHashCode = tvChildren.displayHashCodeProperty();
        labelPrefWidth = new SimpleDoubleProperty(this, "LabelWidth", -1);
        setActiveItem = this::setCurrent;
        tvChildren.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectTableView();
            }
        });
        tvChildren.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) selectTableView();
        });
    }

    public ObjExplorer(T root) {
        this();
        setRoot(root);
    }

    private void selectTableView() {
        T selectedItem = (T) tvChildren.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ObjectDisplay<T> d = new ObjectDisplay<>();
            d.setText(selectedItem.toString());
            d.setActionConsumer(setActiveItem);
            d.setValue(selectedItem);
            setCurrent(d);
        }
    }

    public T getRoot() {
        return root;
    }

    public void setRoot(T root) {
        this.root = root;
        abPath.clear();
        ObjectDisplay<T> d = new ObjectDisplay<>();
        if (root instanceof Map || root instanceof Collection || root.getClass().isArray())
            d.setText(root.getClass().getSimpleName());
        else d.setText(root.toString());
        d.setActionConsumer(setActiveItem);
        d.setValue(root);
        setCurrent(d);
    }

    public void load() {
        if (current.getValue() instanceof Map) {
            tvChildren.setContent(new ArrayList<>(((Map<?, T>) current.getValue()).values()));
        } else if (current.getValue() instanceof Collection) {
            tvChildren.setContent(new ArrayList<>((Collection<T>) current.getValue()));
        } else if (current.getValue().getClass().isArray()) {
            List<T> list = new ArrayList<>();
            int len = Array.getLength(current.getValue());
            for (int i = 0; i < len; i++) {
                list.add((T) Array.get(current.getValue(), i));
            }
            tvChildren.setContent(list);
        } else {
            tvChildren.setContent(null);
        }
        vbProperties.getChildren().clear();
        Class c = current.getValue().getClass();
        Map<String, Method> getters = ClassUtils.getGetters(c);
        List<String> keys = new ArrayList<>(getters.keySet());
        for (String key : keys) {
            Class<?> propType = getters.get(key).getReturnType();
            if (key.equals("hashCode") && !displayHashCode.get()) continue;
            if (key.equals("Class") && !displayClass.get()) continue;
            String displayLabel = TitleStyle.transform(key, titleStyle.get());
            PropertyDisplay<T> propertyDisplay;
            if (propType.isPrimitive() || propType == String.class) {
                propertyDisplay = new ValueDisplay<>();
            } else {
                ObjectDisplay<T> od = new ObjectDisplay<>();
                od.setActionConsumer(setActiveItem);
                od.setText(key);
                propertyDisplay = od;
            }
            propertyDisplay.setLabel(displayLabel + ": ");
            propertyDisplay.labelPrefWidthProperty().bindBidirectional(labelPrefWidth);
            try {
                propertyDisplay.setValue((T) getters.get(key).invoke(current.getValue()));
                vbProperties.getChildren().add(propertyDisplay);
            } catch (IllegalAccessException | InvocationTargetException e) {
//                Cannot get value
            }
        }
        vbProperties.requestFocus();
        layout();
        Platform.runLater(() -> {
            propertiesPane.setMaxHeight(vbProperties.getHeight() + propertiesPane.getPadding().getTop() + propertiesPane.getPadding().getBottom() + 20);
            if (propertiesPane.getMaxHeight() > propertiesPane.getHeight()) {
                double d = splitPane.getHeight() / propertiesPane.getMaxHeight();
                splitPane.setDividerPositions(d > 2 ? d : 0.5);
            }
        });
        if (onLoad != null) onLoad.run();
    }

    public ObjectDisplay<T> getCurrent() {
        return current;
    }

    public void setCurrent(ObjectDisplay<T> current) {
        if (current != null) {
            if (current.getValue() == null) {
                alert.show();
            } else {
                this.current = current;
                abPath.setCurrent(current);
                load();
            }
        }
    }

    public TitleStyle getTitleStyle() {
        return titleStyle.get();
    }

    public void setTitleStyle(TitleStyle titleStyle) {
        this.titleStyle.set(titleStyle);
    }

    public boolean isDisplayClass() {
        return displayClass.get();
    }

    public void setDisplayClass(boolean displayClass) {
        this.displayClass.set(displayClass);
    }

    public boolean isDisplayHashCode() {
        return displayHashCode.get();
    }

    public void setDisplayHashCode(boolean displayHashCode) {
        this.displayHashCode.set(displayHashCode);
    }

    public double getLabelPrefWidth() {
        return labelPrefWidth.get();
    }

    public void setLabelPrefWidth(double labelPrefWidth) {
        this.labelPrefWidth.set(labelPrefWidth);
    }

    public DoubleProperty labelPrefWidthProperty() {
        return labelPrefWidth;
    }

    public TableViewX<T> getTvChildren() {
        return tvChildren;
    }

    public Runnable getOnLoad() {
        return onLoad;
    }

    public void setOnLoad(Runnable onLoad) {
        this.onLoad = onLoad;
    }
}
