# fx-object-explorer
A javafx control for browsing object structure and data

### Usage
##### Maven
```xml
<dependency>
    <groupId>io.github.quellatalo.fx</groupId>
    <artifactId>object-explorer</artifactId>
    <version>1.1.0.0</version>
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
        ObjExplorer explorer = new ObjExplorer(yourClasses);
        // If the object you want to explore is an Array/collection, you can also set prefered display type:
        // ObjExplorer explorer = new ObjExplorer(yourClasses, YourClass.class);
        
        // add to scene and stage
        primaryStage.setScene(new Scene(explorer));
        primaryStage.show();
        
        // you can also pop a new separate browser like this:
        new SimpleBrowser<>(yourClasses).show();
    }
}
```