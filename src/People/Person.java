package People;

public class Person {

    protected final String name;
    protected String reason;

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, String reason) {
        this.name = name;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        String str = String.format("Name: %s", name);;
        if(reason != null && !reason.isEmpty())
            str = str.concat(String.format(", Absence: %s", reason));
        return str;
    }
}
