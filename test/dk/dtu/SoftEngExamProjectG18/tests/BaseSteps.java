package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Context.InputContext;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.tests.Util.CmdResponse;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

abstract public class BaseSteps {

    protected final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    protected void callCmd(InputContext context, String method, String[] args) {
        CommandException cmdException = null;
        String response = null;

        try {
            Method m = context.getClass().getMethod(method, String[].class);
            m.invoke(context, (Object) args);
        } catch (InvocationTargetException ite) {
            if(ite.getTargetException() instanceof CommandException) {
                cmdException = (CommandException) ite.getTargetException();
            } else {
                response = "An internal error occurred.";
            }
        } catch (Exception e) {
            response = "An internal error occurred.";
        }

        if(response == null) {
            response = context.getOutput();
        }

        context.resetOutput();

        TestHolder.getInstance().response = new CmdResponse(response, cmdException);
    }

}
