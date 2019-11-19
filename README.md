# fx-object-explorer
A javafx control for browsing object structure and data

### Usage
##### Maven
```xml
<dependency>
    <groupId>io.github.quellatalo.fx</groupId>
    <artifactId>object-explorer</artifactId>
    <version>1.0.5</version>
</dependency>
```
### Sample Code

#### Sample Class
```java
public class YourClass {
    private final int id;
    private final String name;
    private final LocalDateTime localDateTime;

    public YourClass(int id, String name, LocalDateTime localDateTime) {
        this.id = id;
        this.name = name;
        this.localDateTime = localDateTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
```

#### Sample Application
```Java
public class Test extends Application {
    private final String ID = "id";

    @Override
    public void start(Stage primaryStage) throws IOException {
        // create sample data
        List<YourClass> yourClasses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            yourClasses.add(new YourClass(i, "your name " + i, LocalDateTime.now().minusHours(i)));
        }
        // create the explorer
        ObjExplorer<List<YourClass>> explorer = new ObjExplorer<>(yourClasses);
        // or you can use default constructor and explorer.setRoot(yourClasses);
        
        // add to scene and stage
        primaryStage.setScene(new Scene(explorer));
        primaryStage.show();
        
        // you can also pop a new separate browser like this:
        new SimpleBrowser<>(yourClasses).show();
    }
}
```