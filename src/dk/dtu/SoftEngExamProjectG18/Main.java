package dk.dtu.SoftEngExamProjectG18;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Controller.Action;
import dk.dtu.SoftEngExamProjectG18.Controller.Enums.CommandExceptionReason;
import dk.dtu.SoftEngExamProjectG18.Controller.Enums.InputContextType;
import dk.dtu.SoftEngExamProjectG18.Controller.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.Controller.InputContext;
import dk.dtu.SoftEngExamProjectG18.General.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    protected static InputStream inputSource = System.in;
    protected static PrintStream outSource = System.out;

    protected static Scanner inputScanner;

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static void handleCommandException(Throwable t, String usage) {
        if (t instanceof CommandException) {
            CommandException ce = (CommandException) t;

            if (ce.getReason() == CommandExceptionReason.INVALID_ARGUMENTS) {
                outSource.println("Usage: " + usage);
                return;
            }
        }

        outSource.println(t.getMessage());
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static boolean loadData(String dir) {
        if (dir.equals("")) {
            return loadDataInternal();
        }

        try {
            FileReader activities = new FileReader(dir + File.separator + "activities.csv");
            FileReader employees = new FileReader(dir + File.separator + "employees.csv");
            FileReader projects = new FileReader(dir + File.separator + "projects.csv");
            FileReader oooActivities = new FileReader(dir + File.separator + "ooo-activities.csv");
            FileReader workHours = new FileReader(dir + File.separator + "workhours.csv");

            CSVReader.readEmployees(employees);
            CSVReader.readProjects(projects);
            CSVReader.readActivities(activities);
            CSVReader.readOOOActivities(oooActivities);
            CSVReader.readWorkHours(workHours);

            return true;
        } catch (FileNotFoundException ignored) {
        }

        outSource.println("One or more data files are missing.");
        return false;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static boolean loadDataInternal() {
        ClassLoader cl = Main.class.getClassLoader();

        InputStream activities = cl.getResourceAsStream("data/activities.csv");
        InputStream employees = cl.getResourceAsStream("data/employees.csv");
        InputStream projects = cl.getResourceAsStream("data/projects.csv");
        InputStream oooActivities = cl.getResourceAsStream("data/ooo-activities.csv");
        InputStream workHours = cl.getResourceAsStream("data/workhours.csv");

        boolean success = activities != null
            && employees != null
            && projects != null
            && oooActivities != null
            && workHours != null;

        if (success) {
            CSVReader.readEmployees(new InputStreamReader(employees));
            CSVReader.readProjects(new InputStreamReader(projects));
            CSVReader.readActivities(new InputStreamReader(activities));
            CSVReader.readOOOActivities(new InputStreamReader(oooActivities));
            CSVReader.readWorkHours(new InputStreamReader(workHours));
        }

        return success;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static boolean redirectBasicInput(String[] input) {
        if (input[0].equals("help")) {
            help();
            return true;
        }

        if (input[0].equals("quit")) {
            quit();
            return true;
        }

        return false;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static void runAction(Action action, ArrayList<String> args) {
        InputContext inputContext = Application.getInstance().getContext();

        try {
            action.run(args.toArray(String[]::new));
            outSource.println(inputContext.getOutput());
        } catch (Exception e) {
            handleCommandException(e, action.getFullSignature());
        }

        inputContext.resetOutput();
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static boolean setupContext(String context) {
        for (InputContextType type : InputContextType.values()) {
            if (context.equalsIgnoreCase(type.toString())) {
                Application.init(type);
                return true;
            }
        }

        outSource.println("This context is not available.");
        return false;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static boolean signIn(String ID) {
        try {
            Application.getInstance().setSignedInEmployee(ID);
            return true;
        } catch (IllegalArgumentException e) {
            outSource.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static String[] splitInput(String input) {
        boolean splitAllowed = true;
        ArrayList<String> tokens = new ArrayList<>();
        String[] inputSplit = input.split("");

        StringBuilder current = new StringBuilder();
        for (String c : inputSplit) {
            if (splitAllowed && c.equals(" ")) {
                tokens.add(current.toString());
                current.setLength(0);
                continue;
            }

            if (c.equals("\"")) {
                splitAllowed = !splitAllowed;
                continue;
            }

            current.append(c);
        }
        tokens.add(current.toString());

        return tokens.toArray(String[]::new);
    }

    /*
        Basic commands
     */

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static void help() {
        ArrayList<String> usages = new ArrayList<>();
        for (Action action : Application.getInstance().getContext().getTriggers().values()) {
            usages.add(action.getFullSignature());
        }

        Collections.sort(usages);

        outSource.println("Available commands:");
        for (String usage : usages) {
            outSource.println(" - " + usage);
        }
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static void quit() {
        outSource.println("Bye!");

        if (inputScanner != null) {
            inputScanner.close();
        }
    }

    /*
        Main
     */

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            outSource.println("Usage: java -jar 02161ExamProject {Employee Initials} {Context=Emp/PM)} [Data folder/N]");
            return;
        }

        if (!setupContext(args[1]) || !(args.length >= 3 ? loadData(args[2]) : loadData("")) || !signIn(args[0])) {
            return;
        }

        Application application = Application.getInstance();

        outSource.println("Welcome, " + application.getSignedInEmployee().getName() + ". You are now signed in as (and acting as) " + application.getContext().getSingularContextName() + ".");
        outSource.println("Type 'help' to view available commands within this context.");
        outSource.println("Multi word arguments can be passed using \"quotes\". Dates are parsed/presented using format yyyy-MM-dd.");
        outSource.println();

        inputScanner = new Scanner(inputSource);
        try {
            while (inputScanner.hasNextLine()) {
                redirectInput(splitInput(inputScanner.nextLine().trim()));
            }
        } catch (IllegalStateException ignored) {} // Thrown when quitting
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static void redirectInput(String[] input) {
        if (input.length == 0 || redirectBasicInput(input)) {
            return;
        }

        ArrayList<String> inputVariants = new ArrayList<>();
        inputVariants.add(input[0]);

        for (int i = 1; i < input.length; i++) {
            inputVariants.add(inputVariants.get(inputVariants.size() - 1) + " " + input[i]);
        }

        InputContext inputContext = Application.getInstance().getContext();

        for (int i = 0; i < inputVariants.size(); i++) {
            String variant = inputVariants.get(i).toLowerCase();

            if (!inputContext.getTriggers().containsKey(variant)) {
                continue;
            }

            Action action = inputContext.getTriggers().get(variant);
            ArrayList<String> args = new ArrayList<>(Arrays.asList(input).subList(i + 1, inputVariants.size()));

            runAction(action, args);

            return;
        }

        outSource.println("Command not found.");
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static void setInSource(InputStream is) {
        inputSource = is;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static void setOutSource(PrintStream ps) {
        outSource = ps;
    }

}
