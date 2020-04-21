package dk.dtu.SoftEngExamProjectG18.tests.Util;

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



}
