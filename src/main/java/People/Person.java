package People;

import People.Role.DPS;
import People.Role.Healer;
import People.Role.Tank;

import java.util.HashMap;

public class Person implements Comparable<Person> {

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

    @Override
    public int compareTo(Person p) {
        String roleA = "None", roleB = "None";
        HashMap<String, Integer> roleMap = new HashMap<>();
        roleMap.put("Tank", 0);
        roleMap.put("Healer", 1);
        roleMap.put("DPS", 2);
        roleMap.put("None", 3);
        if(this instanceof Tank)
            roleA = "Tank";
        else if(this instanceof Healer)
            roleA = "Healer";
        else if(this instanceof DPS)
            roleA = "DPS";
        if(p instanceof Tank)
            roleB = "Tank";
        else if(p instanceof Healer)
            roleB = "Healer";
        else if(p instanceof DPS)
            roleB = "DPS";
        return roleMap.get(roleA) - roleMap.get(roleB);
    }
}
