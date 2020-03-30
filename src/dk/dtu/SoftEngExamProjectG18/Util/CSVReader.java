package dk.dtu.SoftEngExamProjectG18.Util;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class CSVReader {

    protected static boolean getBoolean(HashMap<String, String> entry, String property, boolean def) {
        String val = entry.get(property);
        return val != null ? Boolean.parseBoolean(val) : def;
    }

    protected static Date getDate(HashMap<String, String> entry, String property) {
        String[] date = entry.getOrDefault(property, "").split("-");

        if(date.length == 3) {
            try {
                Calendar c = new GregorianCalendar();
                c.set(Calendar.YEAR, Integer.parseInt(date[0]));
                c.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));

                return c.getTime();
            } catch(Exception ignored) {}
        }

        return null;
    }

    protected static int getInt(HashMap<String, String> entry, String property, int def) {
        try {
            return Integer.parseInt(entry.get(property));
        } catch (NumberFormatException ignored) {}
        return def;
    }

    protected static Date getDateFromYearWeek(HashMap<String, String> entry, String property) {
        String[] weekString = entry.getOrDefault(property, "").split("-");

        if(weekString.length != 2) {
            return null;
        }

        try {
            int year = Integer.parseInt(weekString[0]);
            int week = Integer.parseInt(weekString[1]);

            Calendar c = new GregorianCalendar();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.WEEK_OF_YEAR, week);

            return c.getTime();
        } catch (NumberFormatException ignored) {}

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

                for(int i = 0; i < Math.min(properties.length, values.length); i++) {
                    line.put(properties[i], values[i]);
                }

                lines.add(line);
            }
        } catch (IOException ignored) {}

        return lines;
    }

    public static void readEmployees(Reader fileReader) {
        CompanyDB db = CompanyDB.getInstance();
        ArrayList<HashMap<String, String>> employees = readFile(fileReader);

        for(HashMap<String, String> employee : employees) {
            String ID = employee.get("ID");

            if(ID == null || ID.length() == 0) {
                continue;
            }

            String name = employee.getOrDefault("Name", "");
            int cap = getInt(employee, "WeeklyActivityCap", 10);

            db.getEmployees().put(ID, new Employee(ID, name, cap));
        }
    }

    public static void readProjects(Reader fileReader) {
        CompanyDB db = CompanyDB.getInstance();
        ArrayList<HashMap<String, String>> projects = readFile(fileReader);

        for(HashMap<String, String> project : projects) {
            String name = project.get("Name");
            Date createdAt = getDate(project, "CreatedAt");

            if(name == null || createdAt == null) {
                continue;
            }

            boolean isBillable = getBoolean(project, "IsBillable", true);
            Employee PM = db.getEmployee(project.getOrDefault("PM", null));

            Project p = new Project(name, createdAt, isBillable, PM);
            db.getProjects().put(p.getID(), p);
        }
    }

    public static void readActivities(Reader fileReader) {
        CompanyDB db = CompanyDB.getInstance();
        ArrayList<HashMap<String, String>> activities = readFile(fileReader);

        for(HashMap<String, String> activity : activities) {
            String projectID = activity.get("Project ID");
            String name = activity.get("Name");

            Project project = db.getProject(projectID);
            if(project == null) {
                continue;
            }

            Activity activityInstance = new Activity(name, project);

            Date start = getDateFromYearWeek(activity, "StartWeek");
            Date end = getDateFromYearWeek(activity, "EndWeek");
            if(start != null && end != null) {
                activityInstance.setStartWeek(start);
                activityInstance.setEndWeek(end);
            }

            activityInstance.setDone(getBoolean(activity, "IsDone", false));
        }
    }

    public static void readWorkHours(Reader fileReader) {
        CompanyDB db = CompanyDB.getInstance();
        ArrayList<HashMap<String, String>> workHours = readFile(fileReader);

        for (HashMap<String, String> entry : workHours) {
            String employeeID = entry.get("Employee ID"), projectID = entry.get("Project ID");
            int activityID = getInt(entry, "Activity ID", 0);

            Employee employee = db.getEmployee(employeeID);
            Project project = db.getProject(projectID);

            if (employee != null && project != null) {
                Activity activity = project.getActivity(activityID);

                Date date = getDate(entry, "Date");
                int minutes = getInt(entry, "Minutes", 0);

                if (date != null && minutes >= 0) {
                    EmployeeActivityIntermediate eai = new EmployeeActivityIntermediate(employee, activity);
                    eai.setMinutes(date, minutes);
                }
            }
        }
    }
}
