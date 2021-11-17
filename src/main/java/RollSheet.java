import People.Person;
import People.Role.DPS;
import People.Role.Healer;
import People.Role.Tank;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RollSheet {

    private ArrayList<Person> attendees;
    private ArrayList<Person> absentees;
    private String fileName;

    public RollSheet(ArrayList<Person> attendees, ArrayList<Person> absentees, String fileName) {
        this.attendees = attendees;
        this.absentees = absentees;
        this.fileName = fileName;
    }

    public RollSheet(String fileName) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        final HashMap<String, String> args = new HashMap<>();
        String name, role, type, reason;
        Person person;
        boolean attending = true;
        this.fileName = fileName;
        attendees = new ArrayList<>();
        absentees = new ArrayList<>();
        try {
            for(String str = reader.readLine(); str != null; str = reader.readLine()) {
                if(str.equals("ATTENDEES")) {
                    attending = true;
                    continue;
                } else if(str.equals("ABSENTEES")) {
                    attending = false;
                    continue;
                }
                Arrays.asList(str.split(",")).forEach(i -> args.put(i.split(":")[0].trim(), i.split(":")[1].trim()));
                name = args.get("Name");
                role = (args.get("Role") == null) ? "" : args.get("Role");
                type = args.get("Type");
                reason = args.get("Absence");
                switch(role.toUpperCase()) {
                    case("DPS") -> person = new DPS(name, type, reason);
                    case("TANK") -> person = new Tank(name, reason);
                    case("HEALER") -> person = new Healer(name, type, reason);
                    default -> person = new Person(name, reason);
                }
                if(attending)
                    attendees.add(person);
                else
                    absentees.add(person);
                args.clear();
            }
        } catch(IOException e) {
            System.out.println("Error in reading file.");
        }
    }

    public RollSheet() {
        attendees = new ArrayList<>();
        absentees = new ArrayList<>();
    }

    public ArrayList<Person> getAttendees() {
        return attendees;
    }

    public ArrayList<Person> getAbsentees() {
        return absentees;
    }

    public void setAttendees(ArrayList<Person> list) { this.attendees = list; }

    public void setAbsentees(ArrayList<Person> list) { this.absentees = list; }

    public void setFileName(String fileName) { this.fileName = fileName; }

    public String raidReady(){
        StringBuilder builder = new StringBuilder();
        int healers = 0, tanks = 0, dps = 0;
        for(Person person : attendees) {
            if(person instanceof Healer)
                ++healers;
            if(person instanceof Tank)
                ++tanks;
            if(person instanceof DPS)
                ++dps;
        }
        if(healers >= 2 && tanks >= 2 && dps >= 4)
            return "Raid is ready.";
        else {
            builder.append("Raid is not ready. Needs the following:");
            if(healers < 2) builder.append("\n").append(2 - healers).append(" healers.");
            if(tanks < 2) builder.append("\n").append(2 - tanks).append(" tanks.");
            if(dps < 4) builder.append("\n").append(4 - dps).append(" dps.");
            return builder.toString();
        }
    }

    public String raidOptimal(){
        StringBuilder builder = new StringBuilder();
        int healers = 0, tanks = 0, dps = 0;
        boolean shield = false, regen = false, melee = false, magic = false, ranged = false;
        for(Person person : attendees) {
            if(person instanceof Healer) {
                ++healers;
                if(((Healer) person).getType() == Healer.Type.REGEN)
                    regen = true;
                if(((Healer) person).getType() == Healer.Type.SHIELD)
                    shield = true;
            }
            if(person instanceof Tank)
                ++tanks;
            if(person instanceof DPS) {
                ++dps;
                if(((DPS) person).getType() == DPS.Type.MELEE)
                    melee = true;
                if(((DPS) person).getType() == DPS.Type.MAGIC)
                    magic = true;
                if(((DPS) person).getType() == DPS.Type.RANGED)
                    ranged = true;
            }
        }
        if(shield && regen && melee && magic && ranged)
            return "Raid is optimal.";
        else {
            builder.append("Raid is not optimal. Needs the following:");
            if(!regen) builder.append("\n").append("Regen healer.");
            if(!shield) builder.append("\n").append("Shield healer.");
            if(!melee) builder.append("\n").append("Melee dps.");
            if(!magic) builder.append("\n").append("Magic dps.");
            if(!ranged) builder.append("\n").append("Ranged dps.");
            return builder.toString();
        }
    }

    public void addAttendee(Person attendee) {
        for(Person person : attendees) {
            if(person.getName().equals(attendee.getName())) {
                System.out.println("Error: Duplicate entry");
                return;
            }
        }
        attendees.add(attendee);
        removeAbsentee(findAbsentee(attendee.getName()));
    }

    public void addAbsentee(Person absentee) {
        for(Person person : absentees) {
            if(person.getName().equals(absentee.getName())) {
                System.out.println("Error: Duplicate entry");
                return;
            }
        }
        absentees.add(absentee);
        removeAttendee(findAttendee(absentee.getName()));
    }

    public boolean removeAttendee(Person attendee) {
        return attendees.remove(attendee);
    }

    public boolean removeAbsentee(Person absentee) {
        return absentees.remove(absentee);
    }

    public Person findPerson(String name) {
        Person person = findAttendee(name);
        if(person == null)
            person = findAbsentee(name);
        return person;
    }

    private Person findAttendee(String name) {
        for(Person person : attendees) {
            if(person.getName().equalsIgnoreCase(name))
                return person;
        }
        return null;
    }

    private Person findAbsentee(String name) {
        for(Person person : absentees) {
            if(person.getName().equalsIgnoreCase(name))
                return person;
        }
        return null;
    }

    public void save() throws IOException {
        PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8);
        writer.println("ATTENDEES");
        attendees.forEach(writer::println);
        writer.println("ABSENTEES");
        absentees.forEach(writer::println);
        writer.close();
    }
}
