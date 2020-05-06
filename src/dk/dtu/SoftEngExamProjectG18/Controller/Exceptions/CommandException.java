package dk.dtu.SoftEngExamProjectG18.Controller.Exceptions;

import dk.dtu.SoftEngExamProjectG18.Controller.Enums.CommandExceptionReason;

public class CommandException extends Exception {

    protected CommandExceptionReason reason;

    public CommandException(CommandExceptionReason cer) {
        this(cer, "An error occurred.");
    }

    public CommandException(String message) {
        this(CommandExceptionReason.EXECUTION_ERROR, message);
    }

    public CommandException(CommandExceptionReason cer, String message) {
        super(message);

        this.reason = cer;
    }

    public CommandExceptionReason getReason() {
        return reason;
    }

}
