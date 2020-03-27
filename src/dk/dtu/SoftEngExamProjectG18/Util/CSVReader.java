package dk.dtu.SoftEngExamProjectG18.Util;

import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;

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

    }

    public static void readWorkHours(Reader fileReader) {

    }


}
