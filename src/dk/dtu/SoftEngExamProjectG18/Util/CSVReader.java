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

            if(ID == null) {
                continue;
            }

            String name = employee.getOrDefault("Name", "");
            int cap = Integer.parseInt(employee.getOrDefault("WeeklyActivityCap", "10"));

            db.getEmployees().put(ID, new Employee(ID, name, cap));
        }
    }

    public static void readProjects(Reader fileReader) {
        CompanyDB db = CompanyDB.getInstance();
        ArrayList<HashMap<String, String>> projects = readFile(fileReader);

        for(HashMap<String, String> project : projects) {
            String name = project.get("Name");
            if(name == null) {
                continue;
            }

            String[] date = project.get("CreatedAt").split("-");
            if(date.length != 3) {
                continue;
            }

            Date createdAt = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));

            boolean isBillable = Boolean.parseBoolean(project.getOrDefault("IsBillable", "true"));
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

            if(projectID == null || name == null) {
                continue;
            }

            Project project = db.getProject(projectID);
            if(project == null) {
                continue;
            }

            Activity activityInstance = new Activity(name, project);

            String[] startWeekString = activity.get("StartWeek").split("-");
            String[] endWeekString = activity.get("EndWeek").split("-");
            if(startWeekString.length == 2 && endWeekString.length == 2) {
                try {
                    int startYear = Integer.parseInt(startWeekString[0]);
                    int startWeek = Integer.parseInt(startWeekString[1]);

                    int endYear = Integer.parseInt(endWeekString[0]);
                    int endWeek = Integer.parseInt(endWeekString[1]);

                    Calendar startCalendar = new GregorianCalendar();
                    startCalendar.set(Calendar.YEAR, startYear);
                    startCalendar.set(Calendar.WEEK_OF_YEAR, startWeek);
                    Date start = startCalendar.getTime();

                    Calendar endCalendar = new GregorianCalendar();
                    endCalendar.set(Calendar.YEAR, endYear);
                    endCalendar.set(Calendar.WEEK_OF_YEAR, endWeek);
                    Date end = endCalendar.getTime();

                    activityInstance.setStartWeek(start);
                    activityInstance.setEndWeek(end);
                } catch(NumberFormatException ignored) {}
            }

            activityInstance.setDone(Boolean.parseBoolean(activity.get("IsDone")));
        }
    }

    public static void readWorkHours(Reader fileReader) {
        CompanyDB db = CompanyDB.getInstance();
        ArrayList<HashMap<String, String>> workHours = readFile(fileReader);

        for(HashMap<String, String> entry : workHours) {
            try {
                // Employee ID, Project ID, Activity ID, Date, Minutes
                String employeeID = entry.get("Employee ID");
                String projectID = entry.get("Project ID");
                int activityID = Integer.parseInt(entry.getOrDefault("Activity ID", "0"));

                Employee employee = db.getEmployee(employeeID);
                Project project = db.getProject(projectID);

                if(employee == null || project == null) {
                    continue;
                }

                Activity activity = project.getActivity(activityID);

                String[] dateString = entry.getOrDefault("Date", "").split("-");
                if(activity == null || dateString.length != 3) {
                    continue;
                }

                Date date = new Date(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));

                int minutes = Integer.parseInt(entry.getOrDefault("Minutes", "0"));

                EmployeeActivityIntermediate eai = new EmployeeActivityIntermediate(employee, activity);
                eai.setMinutes(date, minutes);
            } catch (NumberFormatException ignored) {}
        }
    }


}
