package People.Role;

public class Healer extends Role {

    public enum Type {REGEN, SHIELD}

    Type type;

    public Healer(String name, String type) {
        super(name);
        this.type = Type.valueOf(type.toUpperCase());
    }

    public Healer(String name, String type, String reason) {
        super(name, reason);
        this.type = Type.valueOf(type.toUpperCase());
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        String str = String.format("Role: %s, Type: %s, Name: %s", "Healer", type, name);
        if(reason != null && !reason.isEmpty())
            str = str.concat(String.format(", Absence: %s", reason));
        return str;
    }
}
