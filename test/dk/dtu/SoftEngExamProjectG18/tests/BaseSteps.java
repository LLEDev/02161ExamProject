package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Context.InputContext;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.Interfaces.ThrowingFunction;
import dk.dtu.SoftEngExamProjectG18.tests.Util.CmdResponse;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import org.junit.Assert;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

abstract public class BaseSteps {

    protected void callCmd(InputContext context, ThrowingFunction<String[]> tf, String[] args) {
        AtomicReference<Exception> atomicException = new AtomicReference<>();
        String response = null;

        Consumer<Exception> exceptionHook = atomicException::set;
        context.addCommandExceptionHook(exceptionHook);

        try {
            tf.apply(args);
        } catch (Exception e) {
            handleNonCommandException(e);
            return;
        }

        context.removeCommandExceptionHook(exceptionHook);

        Exception exception = atomicException.get();
        if(exception != null && !(exception instanceof CommandException)) {
            handleNonCommandException(exception);
            return;
        }

        CommandException commandException = (CommandException) exception;

        response = context.getOutput();
        context.resetOutput();

        TestHolder.getInstance().setResponse(new CmdResponse(response, commandException));
    }

    protected void handleNonCommandException(Exception e) {
        e.printStackTrace();
        Assert.fail("A non-expected exception was thrown.");
    }

}
