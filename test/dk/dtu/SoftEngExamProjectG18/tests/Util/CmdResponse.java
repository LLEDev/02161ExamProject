package dk.dtu.SoftEngExamProjectG18.tests.Util;

import dk.dtu.SoftEngExamProjectG18.Controller.Enums.CommandExceptionReason;
import dk.dtu.SoftEngExamProjectG18.Controller.Exceptions.CommandException;

public class CmdResponse {

    protected CommandException ce;
    protected String response;

    /**
     * @author Someone
     */
    public CmdResponse(String response, CommandException ce) {
        this.ce = ce;
        this.response = response;
    }

    /**
     * @author Someone
     */
    public CommandException getCommandException() {
        return this.ce;
    }

    /**
     * @author Someone
     */
    public String getResponse() {
        return this.response;
    }

    /**
     * @author Someone
     */
    public boolean exceptionMessageIs(String message) {
        if (this.ce == null || this.ce.getMessage() == null) {
            return false;
        }

        return this.ce.getMessage().equals(message);
    }


}
