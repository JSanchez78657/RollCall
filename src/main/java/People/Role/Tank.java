package People.Role;

public class Tank extends Role {

    public Tank(String name) {
        super(name);
    }

    public Tank(String name, String reason) {
        super(name, reason);
    }

    @Override
    public String toString() {
        String str = String.format("Role: %s, Name: %s", "Tank", name);
        if(reason != null && !reason.isEmpty())
            str = str.concat(String.format(", Absence: %s", reason));
        return str;
    }
}
