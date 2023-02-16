package planner;

public class QualifiedName {
    public QualifiedName(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 1) {
            this.name = parts[0];
            this.function = "";

        } else {
            this.name = parts[0];
            this.function = parts[1];

        }
    }

    public String function;
    public String name;
}
