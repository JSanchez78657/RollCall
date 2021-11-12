import People.Person;
import People.Role.DPS;
import People.Role.Healer;
import People.Role.Tank;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Starter {

    private static String input = "";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        RollSheet sheet;
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("M-d-y")) + ".raid";
        System.out.println("Welcome to the Roll Sheet");
        try {
            sheet = new RollSheet(filename);
            if(confirmYes("There is a Roll Sheet already created for today, would you like to load it?"))
                manageSheet(sheet, false);
            mainMenu();
        } catch(FileNotFoundException f) {
            mainMenu();
        }
    }

    private static void mainMenu() {
        do {
            System.out.println();
            System.out.println("-----------Main Menu------------");
            System.out.println("LOAD a sheet");
            System.out.println("START a new sheet");
            System.out.println("QUIT");
            input = scanner.next().toUpperCase();
            switch (input) {
                case ("LOAD") -> loadSheet();
                case ("START") -> startSheet();
            }
        } while(!input.equalsIgnoreCase("QUIT"));
    }

    private static void loadSheet() {
        System.out.println();
        System.out.println("-----------Load Sheet-----------");
        System.out.println("Please enter a filename (CANCEL to exit).");
        input = scanner.next();
        try {
            if(!input.equalsIgnoreCase("CANCEL"))
                manageSheet(new RollSheet(input), false);
        } catch(FileNotFoundException e) {
            System.out.println("Error, no file named \"%s\" found in directory".formatted(input));
        }
    }

    private static void startSheet() {
        RollSheet sheet = new RollSheet();
        System.out.println();
        System.out.println("-----------New Sheet------------");
        System.out.println("Please enter a filename.");
        input = scanner.next();
        sheet.setFileName(input);
        manageSheet(sheet, true);
    }

    private static void manageSheet(RollSheet sheet, boolean changes) {
        do {
            System.out.println();
            System.out.println("----------Manage Sheet----------");
            System.out.println("VIEW current role call");
            System.out.println("ADD a person");
            System.out.println("REMOVE a person");
            System.out.println("SAVE sheet");
            System.out.println("RETURN to main menu");
            input = scanner.next().toUpperCase();
            switch(input) {
                case ("VIEW") -> viewSheet(sheet);
                case ("ADD") -> { addPerson(sheet); changes = true; }
                case ("REMOVE") -> { removePerson(sheet); changes = true; }
                case ("SAVE") -> { saveSheet(sheet); changes = false; }
                case ("RETURN") -> { if(changes) confirmReturn(sheet); }
            }
        } while(!input.equalsIgnoreCase("RETURN"));
    }

    private static void viewSheet(RollSheet sheet) {
        System.out.println();
        System.out.println("----------Roll Sheet----------");
        System.out.println("---ATTENDEES---");
        sheet.getAttendees().forEach(System.out::println);
        System.out.println("---ABSENTEES---");
        sheet.getAbsentees().forEach(System.out::println);
        System.out.println("---STATUS---");
        System.out.println(sheet.raidReady());
        System.out.println(sheet.raidOptimal());
        System.out.println("(Press Enter to continue.)");
        try { System.in.read(); }
        catch (IOException e) { System.out.println("Error in pause."); }
    }

    private static void addPerson(RollSheet sheet) {
        String name, reason = "", role = "", type = "", hold;
        boolean absent;
        Person person;
        do {
            System.out.println();
            System.out.println("---Add Person---");
            System.out.print("Name (Required): ");
            name = scanner.next();
            scanner.nextLine();
            absent = confirmYes("Absentee? (Y/N): ");
            if(absent) {
                System.out.print("Reason: ");
                reason = scanner.nextLine();
            }
            System.out.print("Role: ");
            role = scanner.nextLine();
            if(!role.isEmpty() && !role.equalsIgnoreCase("TANK")) {
                System.out.print("Type: ");
                type = scanner.nextLine();
            }
            try {
                switch (role.toUpperCase()) {
                    case ("DPS") -> person = new DPS(name, type, reason);
                    case ("TANK") -> person = new Tank(name, reason);
                    case ("HEALER") -> person = new Healer(name, type, reason);
                    default -> person = new Person(name, reason);
                }
            } catch(IllegalArgumentException e) {
                person = null;
                if(role.equalsIgnoreCase("DPS")) hold = Arrays.toString(DPS.Type.values());
                else hold = Arrays.toString(Healer.Type.values());
                System.out.println("Error, valid types of %s are:".formatted(role.toUpperCase()));
                System.out.println(hold);
            }
            if(person != null) {
                if (absent)
                    sheet.addAbsentee(person);
                else
                    sheet.addAttendee(person);
            }
        } while(confirmYes("Add another person? (Y/N): "));
    }

    private static void removePerson(RollSheet sheet) {
        String name;
        Person person;
        boolean found;
        do {
            System.out.println();
            System.out.println("---Remove Person---");
            System.out.println("Who would you like to remove from the roll sheet?");
            name = scanner.next();
            person = sheet.findPerson(name);
            if(person == null)
                System.out.println("No person named %s found.".formatted(name));
            else {
                found = sheet.removeAbsentee(person);
                if(!found) sheet.removeAttendee(person);
            }
        } while(confirmYes("Remove another person? (Y/N): "));
    }

    private static void saveSheet(RollSheet sheet) {
        try {
            sheet.save();
        } catch (IOException e) {
            System.out.println("Error in file saving.");
        }
    }

    private static void confirmReturn(RollSheet sheet) {
        if(confirmYes("Would you like to save the sheet before returning?"))
            saveSheet(sheet);
    }

    private static boolean confirmYes(String message) {
        System.out.println(message);
        input = scanner.next();
        return (input.startsWith("Y"));
    }
}
