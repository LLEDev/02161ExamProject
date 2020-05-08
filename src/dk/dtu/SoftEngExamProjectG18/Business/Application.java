package dk.dtu.SoftEngExamProjectG18.Business;

import dk.dtu.SoftEngExamProjectG18.Business.Enums.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.AccessDeniedException;
import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.ExtractionException;
import dk.dtu.SoftEngExamProjectG18.Business.Interfaces.Extractor;
import dk.dtu.SoftEngExamProjectG18.Controller.Enums.InputContextType;
import dk.dtu.SoftEngExamProjectG18.Controller.InputContext;
import dk.dtu.SoftEngExamProjectG18.General.Assertions;
import dk.dtu.SoftEngExamProjectG18.Persistence.CompanyDB;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    protected static Application instance;

    public static void init(String context) throws IllegalArgumentException {
        for (InputContextType ict : InputContextType.values()) {
            if (context.equalsIgnoreCase(ict.toString())) {
                init(ict);
                return;
            }
        }

        throw new IllegalArgumentException(
            String.format(
                "Given context type is not valid. Valid values are: %s.",
                Arrays.stream(InputContextType.values()).map(Enum::toString).collect(Collectors.joining(", "))
            )
        );
    }

    public static void init(InputContextType contextType) throws IllegalArgumentException {
        Assertions.assertOrThrow(
            () -> new IllegalArgumentException("Invalid context given."),
            contextType != null
        );

        instance = new Application(contextType);
    }

    public static Application getInstance() {
        Assertions.assertOrThrow(
            () -> new IllegalStateException("Application has not been initialized."),
            instance != null
        );

        return instance;
    }

    protected InputContext context;
    protected CompanyDB db = new CompanyDB();

    /*
     * Protected methods
     */

    protected CompanyDB getDB() {
        return this.db;
    }

    protected void setDB(CompanyDB db) {
        this.db = db;
    }

    public Application(InputContextType ict) {
        this.context = InputContext.getContext(ict);
    }

    /*
     * CRUD Actions
     */

    public Activity createActivity(String projectID, String name) throws AccessDeniedException {
        Project project = this.getProject(projectID);
        project.assertPM(this.db.getSignedInEmployee());

        return new Activity(name, project);
    }

    public Employee createEmployee(String ID, String name) {
        Employee employee = new Employee(ID, name);

        this.db.addEmployee(employee);

        return employee;
    }

    public Employee createEmployee(String ID, String name, int weeklyActivityCap) {
        Employee employee = new Employee(ID, name, weeklyActivityCap);

        this.db.addEmployee(employee);

        return employee;
    }

    public Project createProject(String name, boolean isBillable) throws IllegalArgumentException {
        int year = (new GregorianCalendar()).get(Calendar.YEAR);
        int nextID = this.db.incrementNextProjectID(year);

        Project project = new Project(nextID, name, isBillable);
        this.db.addProject(project);

        return project;
    }

    public Project createProject(String name, Date createdAt, boolean isBillable, Employee PM) throws IllegalArgumentException {
        int year = (new GregorianCalendar()).get(Calendar.YEAR);
        int nextID = this.db.incrementNextProjectID(year);

        Project project = new Project(nextID, name, createdAt, isBillable, PM);
        this.db.addProject(project);

        return project;
    }

    public <X> ArrayList<HashMap<String, String>> extractData(
        Class<? extends Extractor<X>> extractorClass,
        ArrayList<X> collection
    ) throws ExtractionException {
        return this.extractData(extractorClass, collection, new HashMap<>());
    }

    public <X> ArrayList<HashMap<String, String>> extractData(
        Class<? extends Extractor<X>> extractorClass,
        ArrayList<X> collection,
        HashMap<String, Object> metaData
    ) throws ExtractionException {
        try {
            Extractor<X> extractorInstance = extractorClass.getConstructor().newInstance();
            return extractorInstance.extract(collection, metaData);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new ExtractionException(e.getMessage());
        }
    }

    public InputContext getContext() {
        return this.context;
    }

    public Employee getEmployee(String employeeID) throws IllegalArgumentException {
        Employee employee = db.getEmployee(employeeID);

        if (employee == null) {
            throw new IllegalArgumentException(String.format("The given employee, %s, does not exist.", employeeID));
        }

        return employee;
    }

    public HashMap<String, Employee> getEmployees() {
        return this.db.getEmployees();
    }

    public Project getProject(String projectID) throws IllegalArgumentException {
        Project project = db.getProject(projectID);
        if (project == null) {
            throw new IllegalArgumentException(String.format("The given project, %s, does not exist.", projectID));
        }

        return project;
    }

    public Employee getSignedInEmployee() {
        return this.db.getSignedInEmployee();
    }

    public void setContext(InputContext ic) {
        this.context = ic;
    }

    public void setSignedInEmployee(String employeeID) {
        this.db.setSignedInEmployee(employeeID);
    }

    /*
     * Action helpers
     */

    protected void helperSetSubmitHours(String projectID, int activityID, Date date, double hours, boolean shouldSet) throws AccessDeniedException, IllegalArgumentException {
        Project project = this.getProject(projectID);
        Activity activity = project.getActivity(activityID);
        Employee signedInEmployee = this.db.getSignedInEmployee();

        EmployeeActivityIntermediate employeeActivityIntermediate = EmployeeActivityIntermediate.getAssociation(signedInEmployee, activity);

        int minutes = (int) (hours * 60.0);
        if (shouldSet) {
            employeeActivityIntermediate.setMinutes(date, minutes);
        } else {
            employeeActivityIntermediate.submitMinutes(date, minutes);
        }
    }

    /*
     * Other actions
     */

    public void assignEmployeeToActivity(String employeeID, String projectID, int activityID) throws AccessDeniedException {
        Project project = this.getProject(projectID);
        project.assertPM(this.db.getSignedInEmployee());

        Activity activity = project.getActivity(activityID);
        Employee employee = this.getEmployee(employeeID);

        EmployeeActivityIntermediate.initAssociation(employee, activity);
    }

    public void assignPM(String projectID, String employeeID) throws AccessDeniedException, IllegalArgumentException {
        this.getProject(projectID).assignPM(this.getEmployee(employeeID), this.db.getSignedInEmployee());
    }

    public void estimateActivityDuration(String projectID, int activityID, int numHours) throws IllegalArgumentException {
        Project project = this.getProject(projectID);
        project.getActivity(activityID).setEstimatedHours(numHours);
    }

    public void finishActivity(String projectID, int activityID) throws AccessDeniedException {
        Project project = this.db.getProject(projectID);
        project.assertPM(this.db.getSignedInEmployee());
        project.getActivity(activityID).setDone(true);
    }

    public void requestAssistance(String projectID, int activityID, String employeeID) throws AccessDeniedException, IllegalArgumentException {
        Project project = this.getProject(projectID);
        Activity activity = project.getActivity(activityID);
        Employee employee = this.getEmployee(employeeID);

        assert project != null && activity != null && employee != null : "Precondition 1 of requestAssistance";

        Employee signedInEmployee = db.getSignedInEmployee();
        HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> signedInEmployeeActivities
            = signedInEmployee.getActivities();
        HashMap<Integer, EmployeeActivityIntermediate> signedInEmployeeProjectActivities =
            signedInEmployeeActivities.get(project.getID());

        if (signedInEmployeeProjectActivities == null) {
            String output = String.format("You are not allowed to work with the given project, %s.", projectID);
            throw new AccessDeniedException(output);
        }

        boolean signedInEmployeeIsNotAttachedToActivity = !signedInEmployeeProjectActivities.containsKey(activity.getID());

        if (signedInEmployeeIsNotAttachedToActivity) {
            String output = String.format("You are not allowed to work with the given activity, %s.", activityID);
            throw new AccessDeniedException(output);
        }

        assert signedInEmployeeProjectActivities != null && !signedInEmployeeIsNotAttachedToActivity :
            "Precondition 2 of requestAssistance";

        employee.assertOpenActivities();

        assert employee.getNumOpenActivities() > 0 : "Precondition 3 of requestAssistance";

        employee.addActivity(activity);

        assert employee.getActivities().containsKey(projectID) &&
            employee.getActivities().get(projectID).containsKey(activityID) :
            "Postcondition of requestAssistance";
    }

    public void requestOutOfOffice(OOOActivityType type, Date start, Date end) throws IllegalArgumentException {
        Employee signedInEmployee = this.db.getSignedInEmployee();
        signedInEmployee.addOOOActivity(type, start, end);
    }

    public void setActivityInterval(String projectID, int activityID, Date start, Date end) throws IllegalArgumentException {
        Activity activity = this.getProject(projectID).getActivity(activityID);

        activity.setStartWeek(null);
        activity.setEndWeek(null);

        activity.setStartWeek(start);
        activity.setEndWeek(end);
    }

    public void setHours(String projectID, int activityID, Date date, double setHours) throws AccessDeniedException, IllegalArgumentException {
        this.helperSetSubmitHours(projectID, activityID, date, setHours, true);
    }

    public void submitHours(String projectID, int activityID, Date date, double addedHours) throws AccessDeniedException, IllegalArgumentException {
        this.helperSetSubmitHours(projectID, activityID, date, addedHours, false);
    }

    public void switchContext(String newContext) throws IllegalArgumentException {
        CompanyDB db = this.getDB(); // Save earlier DB
        Application.init(newContext);
        Application.getInstance().setDB(db);
    }
}
