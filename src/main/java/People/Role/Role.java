package People.Role;

import People.Person;

public abstract class Role extends Person {
    public Role(String name) {
        super(name);
    }

    public Role(String name, String reason) {
        super(name, reason);
    }
}
