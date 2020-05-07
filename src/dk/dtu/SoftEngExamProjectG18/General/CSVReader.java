package dk.dtu.SoftEngExamProjectG18.General;

import dk.dtu.SoftEngExamProjectG18.Business.*;
import dk.dtu.SoftEngExamProjectG18.Business.Enums.OOOActivityType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class CSVReader {

    protected static boolean getBoolean(HashMap<String, String> entry, String property, boolean def) {
        String val = entry.get(property);
        return val != null ? Boolean.parseBoolean(val) : def;
    }

    protected static Date getDate(HashMap<String, String> entry, String property) {
        try {
            return DateFormatter.parseDate(entry.get(property));
        } catch (ParseException ignored) {
        }
        return null;
    }

    protected static int getInt(HashMap<String, String> entry, String property, int def) {
        try {
            return Integer.parseInt(entry.get(property));
        } catch (NumberFormatException ignored) {
        }
        return def;
    }

    protected static Date getDateFromYearWeek(HashMap<String, String> entry, String property) {
        try {
            return DateFormatter.parseWeek(entry.getOrDefault(property, ""));
        } catch (ParseException ignored) {
        }
        return null;
    }

    protected static OOOActivityType getOOOActivityType(String input) {
        try {
            return OOOActivityType.valueOf(input);
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }

    protected static String[] processLine(String str) {
        return Arrays.stream(str.split(",")).map(String::trim).toArray(String[]::new);
    }

    protected static ArrayList<HashMap<String, String>> readFile(Reader fileReader) {
        BufferedReader br = new BufferedReader(fileReader);
        ArrayList<HashMap<String, String>> lines = new ArrayList<>();

        try {
            String[] properties = processLine(br.readLine());

            String str;
            while ((str = br.readLine()) != null) {
                String[] values = processLine(str);

                HashMap<String, String> line = new HashMap<>();

                for (int i = 0; i < Math.min(properties.length, values.length); i++) {
                    line.put(properties[i], values[i]);
                }

                lines.add(line);
            }
        } catch (IOException ignored) {}

        return lines;
    }

    public static void readEmployees(Reader fileReader) {
        Application application = Application.getInstance();
        ArrayList<HashMap<String, String>> employees = readFile(fileReader);

        for (HashMap<String, String> employee : employees) {
            String ID = employee.get("ID");

            if (ID == null || ID.length() == 0) {
                continue;
            }

            String name = employee.getOrDefault("Name", "");
            int cap = getInt(employee, "WeeklyActivityCap", 10);

            try {
                application.createEmployee(ID, name, cap);
            } catch (IllegalArgumentException ignored) {} // Ignore broken entries
        }
    }

    public static void readProjects(Reader fileReader) {
        Application application = Application.getInstance();
        ArrayList<HashMap<String, String>> projects = readFile(fileReader);

        for (HashMap<String, String> project : projects) {
            String name = project.get("Name");
            Date createdAt = getDate(project, "CreatedAt");

            if (name == null || createdAt == null) {
                continue;
            }

            boolean isBillable = getBoolean(project, "IsBillable", true);

            try {
                Employee PM = application.getEmployee(project.getOrDefault("PM", null));
                application.createProject(name, createdAt, isBillable, PM);
            } catch (IllegalArgumentException ignored) {
            } // Ignore broken entries
        }
    }

    public static void readActivities(Reader fileReader) {
        Application application = Application.getInstance();
        ArrayList<HashMap<String, String>> activities = readFile(fileReader);

        for (HashMap<String, String> activity : activities) {
            String projectID = activity.get("Project ID");
            String name = activity.get("Name");

            try {
                Project project = application.getProject(projectID);
                Activity activityInstance = new Activity(name, project);

                Date start = getDateFromYearWeek(activity, "StartWeek");
                Date end = getDateFromYearWeek(activity, "EndWeek");
                if (start != null && end != null) {
                    activityInstance.setStartWeek(start);
                    activityInstance.setEndWeek(end);
                }

                activityInstance.setDone(getBoolean(activity, "IsDone", false));
            } catch (IllegalArgumentException ignored) {
            } // Ignore broken entries
        }
    }

    public static void readOOOActivities(Reader fileReader) {
        Application application = Application.getInstance();
        ArrayList<HashMap<String, String>> OOOActivities = readFile(fileReader);

        for (HashMap<String, String> entry : OOOActivities) {
            String employeeID = entry.get("Employee ID");
            String typeID = entry.get("Type");

            try {
                Employee employee = application.getEmployee(employeeID);
                OOOActivityType type = getOOOActivityType(typeID);
                Date start = getDate(entry, "Start");
                Date end = getDate(entry, "End");

                employee.addOOOActivity(type, start, end);
            } catch (IllegalArgumentException ignored) {} // Ignore broken entries
        }
    }

    public static void readWorkHours(Reader fileReader) {
        Application application = Application.getInstance();
        ArrayList<HashMap<String, String>> workHours = readFile(fileReader);

        for (HashMap<String, String> entry : workHours) {
            String employeeID = entry.get("Employee ID"), projectID = entry.get("Project ID");
            int activityID = getInt(entry, "Activity ID", 0);

            try {
                Employee employee = application.getEmployee(employeeID);
                Project project = application.getProject(projectID);

                Activity activity = project.getActivity(activityID);

                Date date = getDate(entry, "Date");
                int minutes = getInt(entry, "Minutes", 0);

                HashMap<Integer, EmployeeActivityIntermediate> alreadyTrackedActivities = employee.getActivities().get(project.getID());

                EmployeeActivityIntermediate eai;
                if (alreadyTrackedActivities != null && alreadyTrackedActivities.containsKey(activity.getID())) {
                    eai = alreadyTrackedActivities.get(activity.getID());
                } else {
                    eai = new EmployeeActivityIntermediate(employee, activity);
                }
                eai.setMinutes(date, minutes);
            } catch (IllegalArgumentException ignored) {
            } // Ignore broken entries
        }
    }
}
