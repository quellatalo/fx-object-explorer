package io.github.quellatalo.fx.explr;

import io.github.quellatalo.fx.explr.propdisplay.ObjectDisplay;
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
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ObjExplorer extends GridPane {
    private static final Alert alert = new Alert(Alert.AlertType.INFORMATION, "NULL");
    @FXML
    private AddressBar abPath;
    @FXML
    private ScrollPane propertiesPane;
    @FXML
    private VBox vbProperties;
    @FXML
    private TableViewX tvChildren;
    @FXML
    private SplitPane splitPane;
    private Object root;
    private ObjectDisplay current;
    private ObjectProperty<TitleStyle> titleStyle;
    private BooleanProperty displayClass;
    private BooleanProperty displayHashCode;
    private DoubleProperty labelPrefWidth;
    private Consumer<ObjectDisplay> setActiveItem;
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

    public ObjExplorer(Object root, Class<?> typeArgument) {
        this();
        setRoot(root, typeArgument);
    }

    public ObjExplorer(Object root) {
        this(root, null);
    }

    private void selectTableView() {
        Object selectedItem = tvChildren.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ObjectDisplay d = new ObjectDisplay();
            d.setText(selectedItem.toString());
            d.setActionConsumer(setActiveItem);
            d.setValue(selectedItem);
            setCurrent(d);
        }
    }

    public Object getRoot() {
        return root;
    }

    public void setRoot(Object root) {
        setRoot(root, null);
    }

    public void setRoot(Object root, Class<?> typeArgument) {
        this.root = root;
        abPath.clear();
        ObjectDisplay d = new ObjectDisplay();
        if (root instanceof Map || root instanceof Collection || root.getClass().isArray())
            d.setText(root.getClass().getSimpleName());
        else d.setText(root.toString());
        d.setActionConsumer(setActiveItem);
        d.setValue(root);
        d.setTypeArgument(typeArgument);
        setCurrent(d);
    }

    public void load() {
        var v = current.getValue();
        if (v instanceof Map) {
            tvChildren.setContent(new ArrayList(((Map) v).values()), current.getTypeArgument());
        } else if (v instanceof Collection) {
            tvChildren.setContent(new ArrayList((Collection) v), current.getTypeArgument());
        } else if (v.getClass().isArray()) {
//            List list = Arrays.asList(v);
            List list = new ArrayList();
            int len = Array.getLength(v);
            for (int i = 0; i < len; i++) {
                list.add(Array.get(v, i));
            }
            tvChildren.setContent(list, current.getTypeArgument());
        } else {
            tvChildren.setContent(null);
        }
        vbProperties.getChildren().clear();
        Class c = v.getClass();
        Map<String, Method> getters = ClassUtils.getGetters(c);
        List<String> keys = new ArrayList<>(getters.keySet());
        for (String key : keys) {
            Class<?> propType = getters.get(key).getReturnType();
            if (key.equals("hashCode") && !displayHashCode.get()) continue;
            if (key.equals("Class") && !displayClass.get()) continue;
            String displayLabel = TitleStyle.transform(key, titleStyle.get());
            PropertyDisplay propertyDisplay;
            if (propType.isPrimitive() || propType == String.class) {
                propertyDisplay = new ValueDisplay();
            } else {
                ObjectDisplay od = new ObjectDisplay();
                od.setActionConsumer(setActiveItem);
                od.setText(key);
                propertyDisplay = od;
                try {
                    ParameterizedType parameterizedType = (ParameterizedType) getters.get(key).getGenericReturnType();
                    od.setTypeArgument((Class<?>) parameterizedType.getActualTypeArguments()[0]);
                } catch (Exception e) {
                    // not generic/parameterized
                }
            }
            propertyDisplay.setLabel(displayLabel + ": ");
            propertyDisplay.labelPrefWidthProperty().bindBidirectional(labelPrefWidth);
            try {
                propertyDisplay.setValue(getters.get(key).invoke(v));
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

    public ObjectDisplay getCurrent() {
        return current;
    }

    public void setCurrent(ObjectDisplay current) {
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

    public TableViewX getTvChildren() {
        return tvChildren;
    }

    public Runnable getOnLoad() {
        return onLoad;
    }

    public void setOnLoad(Runnable onLoad) {
        this.onLoad = onLoad;
    }
}
