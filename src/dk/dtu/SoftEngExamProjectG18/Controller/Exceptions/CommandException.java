package dk.dtu.SoftEngExamProjectG18.Controller.Exceptions;

import dk.dtu.SoftEngExamProjectG18.Controller.Enums.CommandExceptionReason;

public class CommandException extends Exception {

    protected CommandExceptionReason reason;

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public CommandException(CommandExceptionReason cer) {
        this(cer, "An error occurred.");
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public CommandException(String message) {
        this(CommandExceptionReason.EXECUTION_ERROR, message);
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public CommandException(CommandExceptionReason cer, String message) {
        super(message);

        this.reason = cer;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public CommandExceptionReason getReason() {
        return reason;
    }

}
