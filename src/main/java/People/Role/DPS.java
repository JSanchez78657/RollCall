package People.Role;

public class DPS extends Role {

    public enum Type {RANGED, MELEE, MAGIC}

    Type type;

    public DPS(String name, String type) {
        super(name);
        this.type = Type.valueOf(type.toUpperCase());
    }

    public DPS(String name, String type, String reason) {
        super(name, reason);
        this.type = DPS.Type.valueOf(type.toUpperCase());
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        String str = String.format("Role: %s, Type: %s, Name: %s", "DPS", type, name);
        if(reason != null && !reason.isEmpty())
            str = str.concat(String.format(", Absence: %s", reason));
        return str;
    }
}
