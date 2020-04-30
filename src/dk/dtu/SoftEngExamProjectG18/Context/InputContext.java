package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Enum.CommandExceptionReason;
import dk.dtu.SoftEngExamProjectG18.Enum.InputContextType;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.Interfaces.ThrowingFunctionWithoutArgs;
import dk.dtu.SoftEngExamProjectG18.Util.DateFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.function.Consumer;

abstract public class InputContext {

    /*
        Abstract methods
     */

    abstract public String getSingularContextName();

    public static InputContext getContext(InputContextType ict) {
        if (ict == InputContextType.PM) {
            return new ProjectManagerInputContext();
        }

        return new EmployeeInputContext();
    }

    public ArrayList<Consumer<Exception>> onExceptionHooks = new ArrayList<>();
    protected String output = "";
    protected ActionMap triggers = ActionMap.build(new Action[]{
        new Action("project assign pm", new String[]{"projectID", "PMID"}, this::cmdAssignPM),
        new Action("switch context", new String[]{"contextType"}, this::cmdSwitchContext),
    });


    public void assertArgumentsValid(int argsLength, int requiredLength) throws CommandException {
        if(argsLength != requiredLength) {
            throw new CommandException(CommandExceptionReason.INVALID_ARGUMENTS);
        }
    }

    public void assertStringParseDateDoable(String possibleDate) throws CommandException {
        try {
            DateFormatter.parseDate(possibleDate);
        } catch (ParseException e) {
            String output = String.format("Any date must be given in the format %s. Received %s.", DateFormatter.toDatePattern(), possibleDate);
            throw new CommandException(output);
        }
    }

    public void assertStringParseIntDoable(String possibleInt) throws CommandException {
        try {
            Integer.parseInt(possibleInt);
        } catch (NumberFormatException nfe) {
            String output = String.format("Any number must be given as an integer. Received %s.", possibleInt);
            throw new CommandException(output);
        }
    }

    public String getOutput() {
        return this.output;
    }

    public ActionMap getTriggers() {
        return this.triggers;
    }

    public void addCommandExceptionHook(Consumer<Exception> hook) {
        this.onExceptionHooks.add(hook);
    }

    public void removeCommandExceptionHook(Consumer<Exception> hook) {
        this.onExceptionHooks.remove(hook);
    }

    public void resetOutput() {
        this.output = "";
    }

    public ExceptionWrapper wrapExceptions(ThrowingFunctionWithoutArgs tf) {
        return new ExceptionWrapper(this, tf, this.onExceptionHooks);
    }

    public void writeOutput(String s) {
        this.output += s;
    }

     /*
        Shared commands - warnings relating to use of reflection API are suppressed
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

    // String inputContext
    public void cmdSwitchContext(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 1);

        String context = args[0];

        this.wrapExceptions(() -> Application.init(context))
            .outputOnSuccess(() -> "Context switched.")
            .outputOnError(Exception::getMessage)
            .run();
    }

}
