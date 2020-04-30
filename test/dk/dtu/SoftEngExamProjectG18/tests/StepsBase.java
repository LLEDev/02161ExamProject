package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.General.Interfaces.ThrowingFunctionWithoutArgs;
import dk.dtu.SoftEngExamProjectG18.Input.InputContext;
import dk.dtu.SoftEngExamProjectG18.tests.Util.CmdResponse;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestExceptionWrapper;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;

abstract public class StepsBase {

    public void wrap(ThrowingFunctionWithoutArgs tf) {
        TestExceptionWrapper tew = new TestExceptionWrapper(tf);
        tew.run();

        InputContext context = Application.getInstance().getContext();
        String response = context.getOutput();
        TestHolder.getInstance().setResponse(new CmdResponse(response, tew.getCommandException()));

        context.resetOutput();
    }

}