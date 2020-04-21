package dk.dtu.SoftEngExamProjectG18.Exceptions;

import dk.dtu.SoftEngExamProjectG18.Enum.CommandExceptionReason;

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

    public boolean exceptionMessageIs(String message) {
        if(this.getMessage() == null) {
            return false;
        }

        return this.getMessage().equals(message);
    }

    public boolean exceptionReasonIs(CommandExceptionReason cer) {
        if(this.getReason() == null) {
            return false;
        }

        return this.getReason().equals(cer);
    }

    public CommandExceptionReason getReason() {
        return reason;
    }

}
