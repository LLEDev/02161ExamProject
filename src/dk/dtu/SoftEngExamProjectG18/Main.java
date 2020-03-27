package dk.dtu.SoftEngExamProjectG18;

import dk.dtu.SoftEngExamProjectG18.Context.InputContext;
import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Enum.InputContextType;
import dk.dtu.SoftEngExamProjectG18.Util.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Main {

    protected static InputContext inputContext;

    protected static void callMethod(String method, String usage, ArrayList<String> args) {
        try {
            Method m = inputContext.getClass().getMethod(method, String[].class);
            boolean result = (boolean) m.invoke(inputContext, (Object) args.toArray(new String[0]));

            if(result) {
                inputContext.printOutput();
            } else {
                System.out.println(usage);
            }

            inputContext.resetOutput();

        } catch (SecurityException e) {
            System.out.println("Internal error: SecurityException");
        } catch (NoSuchMethodException e) {
            System.out.println("Internal error: NoSuchMethodException");
        } catch (IllegalAccessException e) {
            System.out.println("Internal error: IllegalAccessException");
        } catch (InvocationTargetException e) {
            System.out.println("Internal error: InvocationTargetException");
        }
    }

    protected static boolean loadData() {
        ClassLoader cl = Main.class.getClassLoader();
        
        InputStream activities = cl.getResourceAsStream("data/activities.csv");
        InputStream employees = cl.getResourceAsStream("data/employees.csv");
        InputStream projects = cl.getResourceAsStream("data/projects.csv");
        InputStream workHours = cl.getResourceAsStream("data/workhours.csv");

        if(activities == null || employees == null || projects == null || workHours == null) {
            System.out.println("One or more data files are missing.");
            return false;
        }

        CSVReader.readEmployees(new InputStreamReader(employees));
        CSVReader.readProjects(new InputStreamReader(projects));
        CSVReader.readActivities(new InputStreamReader(activities));
        CSVReader.readWorkHours(new InputStreamReader(workHours));

        return true;
    }

    protected static void redirectInput(String[] input) {
        ArrayList<String> inputVariants = new ArrayList<>();
        inputVariants.add(input[0]);

        for(int i = 1; i < input.length; i++) {
            inputVariants.add(inputVariants.get(inputVariants.size() - 1) + " " + input[i]);
        }

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

        System.out.println("Command not found.");
    }

    protected static boolean setupContext(InputContextType ict) {
        InputContext ic = InputContext.getContext(ict);

        if(ict == null) {
            System.out.println("An error occurred.");
            return false;
        }

        inputContext = ic;
        return true;
    }

    protected static boolean signIn(String ID, String context) {
        CompanyDB db = CompanyDB.getInstance();

        if(!db.setSignedInEmployee(ID)) {
            System.out.println("This employee is not registered in the system.");
            return false;
        }

        for(InputContextType type : InputContextType.values()) {
            if(context.equalsIgnoreCase(type.toString())) {
                return setupContext(type);
            }
        }

        return false;
    }

    public static void main(String[] args) {
        CompanyDB db = CompanyDB.getInstance();

        if(args.length != 2) {
            System.out.println("Usage: java -jar 02161ExamProject [Employee Initials] [Context=(Emp, PM)]");
            return;
        }

        if(!loadData() || !signIn(args[0], args[1])) {
            return;
        }

        System.out.println("Welcome, " + db.getSignedInEmployee().getName() + ".");
        System.out.println("You are now signed in as (and acting as) " + inputContext.getSingularContextName() + ".");

        Scanner s = new Scanner(System.in);
        while(s.hasNextLine()) {
            redirectInput(s.nextLine().trim().split(" "));
        }
    }

}
