package dk.dtu.SoftEngExamProjectG18.tests.Util;

import dk.dtu.SoftEngExamProjectG18.Enum.CommandExceptionReason;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;

public class CmdResponse {

    protected CommandException ce;
    protected String response;

    public CmdResponse(String response, CommandException ce) {
        this.ce = ce;
        this.response = response;
    }

    public boolean isClean() {
        return this.ce == null;
    }

    public CommandException getCommandException() {
        return this.ce;
    }

    public String getResponse() {
        return this.response;
    }

    public boolean exceptionMessageIs(String message) {
        if(this.ce == null || this.ce.getMessage() == null) {
            return false;
        }

        return this.ce.getMessage().equals(message);
    }

    public boolean exceptionReasonIs(CommandExceptionReason cer) {
        if(this.ce == null || this.ce.getReason() == null) {
            return false;
        }

        return this.ce.getReason().equals(cer);
    }


}
