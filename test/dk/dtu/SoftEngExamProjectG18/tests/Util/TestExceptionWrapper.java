package dk.dtu.SoftEngExamProjectG18.tests.Util;

import dk.dtu.SoftEngExamProjectG18.Controller.ExceptionWrapper;
import dk.dtu.SoftEngExamProjectG18.Controller.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.General.Interfaces.ThrowingFunctionWithoutArgs;

public class TestExceptionWrapper extends ExceptionWrapper {

    protected CommandException ce = null;

    protected void onError(Exception e) {
        if (e instanceof CommandException) {
            this.ce = (CommandException) e;
            return;
        }

        throw new RuntimeException(e);
    }

    public TestExceptionWrapper(ThrowingFunctionWithoutArgs tf) {
        super(tf);
        this.exceptionHooks.add(this::onError);
    }

    public CommandException getCommandException() {
        return this.ce;
    }

}
