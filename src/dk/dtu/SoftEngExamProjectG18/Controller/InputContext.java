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
     * @author Lasse Lund-Egmose (s194568)
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
     * @author Lasse Lund-Egmose (s194568)
     */
    public void assertArgumentsValid(int argsLength, int requiredLength) throws CommandException {
        if (argsLength != requiredLength) {
            throw new CommandException(CommandExceptionReason.INVALID_ARGUMENTS);
        }
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
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
     * @author Lasse Lund-Egmose (s194568)
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
     * @author Lasse Lund-Egmose (s194568)
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
     * @author Lasse Lund-Egmose (s194568)
     */
    public void assertStringParseDoubleDoable(String possibleDouble) throws CommandException {
        try {
            Double.parseDouble(possibleDouble);
        } catch (NumberFormatException nfe) {
            String output = String.format("The given number could not be parsed. Received %s.", possibleDouble);
            throw new CommandException(output);
        }
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public String getOutput() {
        return this.output;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public boolean getSandbox() {
        return this.sandbox;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public ActionMap getTriggers() {
        return this.triggers;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void addCommandExceptionHook(Consumer<Exception> hook) {
        this.onExceptionHooks.add(hook);
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void removeCommandExceptionHook(Consumer<Exception> hook) {
        this.onExceptionHooks.remove(hook);
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void resetOutput() {
        this.output = "";
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public ExceptionWrapper wrapExceptions(ThrowingFunctionWithoutArgs tf) {
        return new ExceptionWrapper(tf, this.onExceptionHooks, this.getSandbox());
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void writeOutput(String s) {
        this.output += s;
    }

     /*
        Shared commands - warnings relating to use of reflection API are suppressed
      */

    /**
     * @author Mikkel Theiss Westermann (s194601)
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
     * @author Lasse Lund-Egmose (s194568)
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
