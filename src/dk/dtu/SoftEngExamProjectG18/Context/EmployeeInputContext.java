package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Interfaces.ThrowingFunctionWithoutArgs;
import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.Util.DateFormatter;
import dk.dtu.SoftEngExamProjectG18.Util.Table;

import java.text.ParseException;
import java.util.*;

public class EmployeeInputContext extends InputContext {

    /*
        General
     */

    protected ActionMap triggers = ActionMap.build(super.getTriggers(), new Action[]{
        new Action("hours set", new String[]{"projectID", "activityID", "date", "hours"}, this::cmdSetHours),
        new Action("hours submit", new String[]{"projectID", "activityID", "date", "hours"}, this::cmdSubmitHours),
        new Action("project activity complete", new String[]{"projectID", "activityID"}, this::cmdCompleteActivity),
        new Action("project create", new String[]{"name", "billable"}, this::cmdCreateProject),
        new Action("request assistance", new String[]{"projectID", "activityID", "employeeID"}, this::cmdRequestAssistance),
        new Action("request ooo", new String[]{"type", "start", "end"}, this::cmdRequestOutOfOffice),
        new Action("view submissions", new String[]{}, this::cmdViewSubmissions),
    });

    public String getSingularContextName() {
        return "an employee";
    }

    public ActionMap getTriggers() {
        return this.triggers;
    }

    /*
        Command helpers
     */

    public void cmdSetSubmitHours(String[] args, boolean shouldSet) throws CommandException, ParseException {
        this.assertArgumentsValid(args.length, 4);
        this.assertStringParseDateDoable(args[2]);
        this.assertStringParseIntDoable(args[3]);

        String projectID = args[0];
        int activityID = Integer.parseInt(args[1]);
        Date date = DateFormatter.parseDate(args[2]);
        int hours = Integer.parseInt(args[3]);

        ThrowingFunctionWithoutArgs tf = shouldSet ?
            () -> Application.getInstance().setHours(projectID, activityID, date, hours) :
            () -> Application.getInstance().submitHours(projectID, activityID, date, hours);

        this.wrapExceptions(tf)
            .outputOnSuccess(() -> "Hours " + (shouldSet ? "set." : "submitted."))
            .outputOnError(e -> "An error occurred: " + e.getMessage())
            .run();
    }

    /*
        Commands
     */

    // Command arguments: String projectID, int activityID
    public void cmdCompleteActivity(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 2);

        this.wrapExceptions(() -> Application.getInstance().markActivityDone(args[0], Integer.parseInt(args[1])))
            .outputOnSuccess(() -> "Activity completed.")
            .outputOnError(e -> "An error occurred: " + e.getMessage())
            .run();
    }

    // Command arguments: String name, boolean isBillable
    public void cmdCreateProject(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 2);

        String name = args[0];
        boolean isBillable = Boolean.parseBoolean(args[1]);

        this.wrapExceptions(() -> Application.getInstance().createProject(name, isBillable))
            .outputOnSuccess(() -> "Project created.")
            .outputOnError(e -> String.format("Project not created due to error: %s.", e.getMessage()))
            .run();
    }

    // Command arguments: String projectID, int activityID, String employeeID
    public void cmdRequestAssistance(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 3);

        String projectID = args[0];
        int activityID = Integer.parseInt(args[1]);
        String employeeID = args[2];

        this.wrapExceptions(() -> Application.getInstance().requestAssistance(projectID, activityID, employeeID))
            .outputOnSuccess(() -> "Assistance requested.")
            .outputOnError(e -> "An error occurred: " + e.getMessage())
            .run();
    }

    // Command arguments: OOOActivityType type, Date start, Date end
    public void cmdRequestOutOfOffice(String[] args) throws CommandException, ParseException {
        this.assertArgumentsValid(args.length, 3);
        this.assertStringParseDateDoable(args[1]);
        this.assertStringParseDateDoable(args[2]);

        OOOActivityType type = null;
        for (OOOActivityType OOOType : OOOActivityType.values()) {
            if (args[0].toLowerCase().equals(OOOType.toString().toLowerCase())) {
                type = OOOType;
                break;
            }
        }

        if(type == null) {
            String optionDelimiter = ", ";
            String options = Arrays.stream(OOOActivityType.values())
                .map(t -> optionDelimiter + t.toString().toLowerCase())
                .reduce("", String::concat)
                .substring(optionDelimiter.length());

            String output = String.format(
                "Please specify a valid out-of-office activity type. Valid options are: %s. Received: %s.",
                options,
                args[0]
            );
            throw new CommandException(output);
        }

        final OOOActivityType finalType = type;

        Date start = DateFormatter.parseDate(args[1]);
        Date end = DateFormatter.parseDate(args[2]);

        this.wrapExceptions(() -> Application.getInstance().requestOutOfOffice(finalType, start, end))
            .outputOnSuccess(() -> "Out-of-office activity requested.")
            .outputOnError(e -> "An error occurred: " + e.getMessage())
            .run();
    }

    // Command arguments: String projectID, int activityID, Date date, int setHours
    public void cmdSetHours(String[] args) throws CommandException, ParseException {
        this.cmdSetSubmitHours(args, true);
    }

    // Command arguments: String projectID, int activityID, Date date, int addedHours
    public void cmdSubmitHours(String[] args) throws CommandException, ParseException {
        this.cmdSetSubmitHours(args, false);
    }

    // Command arguments: none
    public void cmdViewSubmissions(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 0);

        Employee signedInEmployee = this.application.getSignedInEmployee();

        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(signedInEmployee);

        this.writeOutput(Table.make(
            "submissions",
            new String[]{"Project ID", "Activity ID", "Tracked hours"},
            employeeList
        ));
    }

}
