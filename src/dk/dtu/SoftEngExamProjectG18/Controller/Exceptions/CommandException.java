package dk.dtu.SoftEngExamProjectG18.Controller.Exceptions;

import dk.dtu.SoftEngExamProjectG18.Controller.Enums.CommandExceptionReason;

public class CommandException extends Exception {

    protected CommandExceptionReason reason;

    /**
     * @author Someone
     */
    public CommandException(CommandExceptionReason cer) {
        this(cer, "An error occurred.");
    }

    /**
     * @author Someone
     */
    public CommandException(String message) {
        this(CommandExceptionReason.EXECUTION_ERROR, message);
    }

    /**
     * @author Someone
     */
    public CommandException(CommandExceptionReason cer, String message) {
        super(message);

        this.reason = cer;
    }

    /**
     * @author Someone
     */
    public CommandExceptionReason getReason() {
        return reason;
    }

}
