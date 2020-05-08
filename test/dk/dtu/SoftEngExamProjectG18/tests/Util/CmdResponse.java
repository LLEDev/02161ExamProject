package dk.dtu.SoftEngExamProjectG18.tests.Util;

import dk.dtu.SoftEngExamProjectG18.Controller.Enums.CommandExceptionReason;
import dk.dtu.SoftEngExamProjectG18.Controller.Exceptions.CommandException;

public class CmdResponse {

    protected CommandException ce;
    protected String response;

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public CmdResponse(String response, CommandException ce) {
        this.ce = ce;
        this.response = response;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public CommandException getCommandException() {
        return this.ce;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public String getResponse() {
        return this.response;
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    public boolean exceptionMessageIs(String message) {
        if (this.ce == null || this.ce.getMessage() == null) {
            return false;
        }

        return this.ce.getMessage().equals(message);
    }


}
