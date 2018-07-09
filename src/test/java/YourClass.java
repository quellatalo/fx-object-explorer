import java.time.LocalDateTime;

public class YourClass {
    private final int id;
    private final String name;
    private final LocalDateTime localDateTime;
    private String[] args = {"a", "b", "c"};

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

    public String[] getArgs() {
        return args;
    }
}
