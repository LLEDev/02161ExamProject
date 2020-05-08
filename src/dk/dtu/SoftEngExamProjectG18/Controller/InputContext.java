package dk.dtu.SoftEngExamProjectG18.Controller;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Controller.Enums.CommandExceptionReason;
import dk.dtu.SoftEngExamProjectG18.Controller.Enums.InputContextType;
import dk.dtu.SoftEngExamProjectG18.Controller.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.General.Assertions;
import dk.dtu.SoftEngExamProjectG18.General.Dates;
import dk.dtu.SoftEngExamProjectG18.General.Interfaces.ThrowingFunctionWithoutArgs;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.function.Consumer;

abstract public class InputContext {

    /*
        Abstract methods
     */

    abstract public String getSingularContextName();

    /**
     * @author Someone
     */
    public static InputContext getContext(InputContextType ict) {
        if (ict == InputContextType.PM) {
            return new ProjectManagerInputContext();
        }

        return new EmployeeInputContext();
    }

    public ArrayList<Consumer<Exception>> onExceptionHooks = new ArrayList<>();
    protected String output = "";
    protected boolean sandbox = false; // true when testing UI (i.e. not running business logic)
    protected ActionMap triggers = ActionMap.build(new Action[]{
        new Action("project assign pm", new String[]{"projectID", "PMID"}, this::cmdAssignPM),
        new Action("switch context", new String[]{"contextType"}, this::cmdSwitchContext),
    });

    /**
     * @author Someone
     */
    public void assertArgumentsValid(int argsLength, int requiredLength) throws CommandException {
        if (argsLength != requiredLength) {
            throw new CommandException(CommandExceptionReason.INVALID_ARGUMENTS);
        }
    }

    /**
     * @author Someone
     */
    public void assertStringParseDateDoable(String possibleDate) throws CommandException {
        try {
            Dates.parseDate(possibleDate);
        } catch (ParseException e) {
            String output = String.format("Any date must be given in the format %s. Received %s.", Dates.toDatePattern(), possibleDate);
            throw new CommandException(output);
        }
    }

    /**
     * @author Someone
     */
    public void assertStringParseWeekDoable(String possibleDate) throws CommandException {
        try {
            Assertions.assertOrThrow(
                () -> new ParseException("Wrong format", 0),
                possibleDate.split("-").length == 2
            );
            Dates.parseWeek(possibleDate);
        } catch (ParseException e) {
            String output = String.format("Any week must be given in the format %s. Received %s.", Dates.toWeekPattern(), possibleDate);
            throw new CommandException(output);
        }
    }

    /**
     * @author Someone
     */
    public void assertStringParseIntDoable(String possibleInt) throws CommandException {
        try {
            Integer.parseInt(possibleInt);
        } catch (NumberFormatException nfe) {
            String output = String.format("Any number must be given as an integer (where -2147483649 < i < 2147483648). Received %s.", possibleInt);
            throw new CommandException(output);
        }
    }

    /**
     * @author Someone
     */
    public String getOutput() {
        return this.output;
    }

    /**
     * @author Someone
     */
    public boolean getSandbox() {
        return this.sandbox;
    }

    /**
     * @author Someone
     */
    public ActionMap getTriggers() {
        return this.triggers;
    }

    /**
     * @author Someone
     */
    public void addCommandExceptionHook(Consumer<Exception> hook) {
        this.onExceptionHooks.add(hook);
    }

    /**
     * @author Someone
     */
    public void removeCommandExceptionHook(Consumer<Exception> hook) {
        this.onExceptionHooks.remove(hook);
    }

    /**
     * @author Someone
     */
    public void resetOutput() {
        this.output = "";
    }

    /**
     * @author Someone
     */
    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }

    /**
     * @author Someone
     */
    public ExceptionWrapper wrapExceptions(ThrowingFunctionWithoutArgs tf) {
        return new ExceptionWrapper(tf, this.onExceptionHooks, this.getSandbox());
    }

    /**
     * @author Someone
     */
    public void writeOutput(String s) {
        this.output += s;
    }

     /*
        Shared commands - warnings relating to use of reflection API are suppressed
      */

    /**
     * @author Someone
     */
    // String projectID, String employeeID
    public void cmdAssignPM(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 2);

        String projectID = args[0];
        String employeeID = args[1];

        this.wrapExceptions(() -> Application.getInstance().assignPM(projectID, employeeID))
            .outputOnSuccess(() -> "Project manager assigned.")
            .outputOnError(Exception::getMessage)
            .run();

    }

    /**
     * @author Someone
     */
    // String inputContext
    public void cmdSwitchContext(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 1);

        String context = args[0];

        this.wrapExceptions(() -> Application.getInstance().switchContext(context))
            .outputOnSuccess(() -> "Context switched.")
            .outputOnError(Exception::getMessage)
            .run();
    }

}
