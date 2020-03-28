package dk.dtu.SoftEngExamProjectG18;

import dk.dtu.SoftEngExamProjectG18.Context.InputContext;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Enum.InputContextType;
import dk.dtu.SoftEngExamProjectG18.Util.CSVReader;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class Main {

    protected static InputStream inputSource = System.in;
    protected static PrintStream outSource = System.out;

    protected static Scanner inputScanner;

    protected static void callMethod(String method, String usage, ArrayList<String> args) throws Exception {
        InputContext inputContext = CompanyDB.getContext();

        Method m = inputContext.getClass().getMethod(method, String[].class);
        boolean result = (boolean) m.invoke(inputContext, (Object) args.toArray(new String[0]));

        if (result) {
            inputContext.printOutput();
        } else {
            outSource.println(usage);
        }

        inputContext.resetOutput();
    }

    protected static boolean loadData(String dir) {
        if(dir.equals("")) {
            return loadDataInternal();
        }

        try {
            FileReader activities = new FileReader(dir + File.separator + "activities.csv");
            FileReader employees = new FileReader(dir + File.separator + "employees.csv");
            FileReader projects = new FileReader(dir + File.separator + "projects.csv");
            FileReader workHours = new FileReader(dir + File.separator + "workhours.csv");

            CSVReader.readEmployees(employees);
            CSVReader.readProjects(projects);
            CSVReader.readActivities(activities);
            CSVReader.readWorkHours(workHours);

            return true;
        } catch (FileNotFoundException ignored) {}

        outSource.println("One or more data files are missing.");
        return false;
    }

    protected static boolean loadDataInternal() {
        ClassLoader cl = Main.class.getClassLoader();

        InputStream activities = cl.getResourceAsStream("data/activities.csv");
        InputStream employees = cl.getResourceAsStream("data/employees.csv");
        InputStream projects = cl.getResourceAsStream("data/projects.csv");
        InputStream workHours = cl.getResourceAsStream("data/workhours.csv");

        assert employees != null;
        assert projects != null;
        assert activities != null;
        assert workHours != null;

        CSVReader.readEmployees(new InputStreamReader(employees));
        CSVReader.readProjects(new InputStreamReader(projects));
        CSVReader.readActivities(new InputStreamReader(activities));
        CSVReader.readWorkHours(new InputStreamReader(workHours));

        return true;
    }

    protected static boolean redirectBasicInput(String[] input) {
        if(input[0].equals("help")) {
            help();
            return true;
        }

        if(input[0].equals("quit")) {
            quit();
            return true;
        }

        return false;
    }

    protected static boolean setupContext(InputContextType ict) {
        InputContext ic = InputContext.getContext(ict);
        CompanyDB.getInstance().setInputContext(ic);
        return true;
    }

    protected static boolean signIn(String ID, String context) {
        CompanyDB db = CompanyDB.getInstance();

        if(!db.setSignedInEmployee(ID)) {
            outSource.println("This employee is not registered in the system.");
            return false;
        }

        for(InputContextType type : InputContextType.values()) {
            if(context.equalsIgnoreCase(type.toString())) {
                return setupContext(type);
            }
        }

        outSource.println("This context is not available.");
        return false;
    }

    /*
        Basic commands
     */

    protected static void help() {
        ArrayList<String> usages = new ArrayList<>();
        for(String[] cmd : CompanyDB.getContext().getTriggers().values()) {
            usages.add(cmd[0]);
        }

        Collections.sort(usages);

        outSource.println("Available commands:");
        for(String usage : usages) {
            outSource.println(" - " + usage);
        }
    }

    protected static void quit() {
        outSource.println("Bye!");

        if(inputScanner != null) {
            inputScanner.close();
        }
    }

    /*
        Main
     */

    public static void main(String[] args) throws Exception {
        CompanyDB db = CompanyDB.getInstance();

        if(args.length < 2) {
            outSource.println("Usage: java -jar 02161ExamProject {Employee Initials} {Context=Emp/PM)} [Data folder/N]");
            return;
        }

        boolean dataLoad = args.length >= 3 ? loadData(args[2]) : loadData("");

        if(!dataLoad || !signIn(args[0], args[1])) {
            return;
        }

        outSource.println("Welcome, " + db.getSignedInEmployee().getName() + ".");
        outSource.println("You are now signed in as (and acting as) " + CompanyDB.getContext().getSingularContextName() + ".");

        inputScanner = new Scanner(inputSource);
        try {
            while (inputScanner.hasNextLine()) {
                redirectInput(inputScanner.nextLine().trim().split(" "));
            }
        } catch(IllegalStateException ignored) {} // Thrown when quitting
    }

    public static void redirectInput(String[] input) throws Exception {
        if(input.length == 0 || redirectBasicInput(input)) {
            return;
        }

        ArrayList<String> inputVariants = new ArrayList<>();
        inputVariants.add(input[0]);

        for(int i = 1; i < input.length; i++) {
            inputVariants.add(inputVariants.get(inputVariants.size() - 1) + " " + input[i]);
        }

        InputContext inputContext = CompanyDB.getContext();

        for(int i = 0; i < inputVariants.size(); i++) {
            String variant = inputVariants.get(i).toLowerCase();

            if(!inputContext.getTriggers().containsKey(variant)) {
                continue;
            }

            String[] trigger = inputContext.getTriggers().get(variant);
            String usage = trigger[0];
            String method = trigger[1];

            int argsFrom = i + 1;
            ArrayList<String> args = new ArrayList<>();
            for(int j = argsFrom; j < inputVariants.size(); j++) {
                args.add(inputVariants.get(j));
            }

            callMethod(method, usage, args);

            return;
        }

        outSource.println("Command not found.");
    }

    public static void setInSource(InputStream is) {
        inputSource = is;
    }

    public static void setOutSource(PrintStream ps) {
        outSource = ps;
    }

}
