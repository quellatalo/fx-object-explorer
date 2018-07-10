import javafx.application.Application;
import javafx.stage.Stage;
import quellatalo.nin.fx.SimpleBrowser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Test extends Application {
    private final String ID = "id";

    @Override
    public void start(Stage primaryStage) throws IOException {
        List<YourClass> classes = new ArrayList<>();
        classes.add(new YourClass(1, "a", LocalDateTime.now()));
        classes.add(new YourClass(2, "b", LocalDateTime.now()));
        classes.add(new YourClass(3, "c", LocalDateTime.now()));
        classes.add(new YourClass(4, "d", LocalDateTime.now()));
        new SimpleBrowser<>(classes).show();
    }
}
