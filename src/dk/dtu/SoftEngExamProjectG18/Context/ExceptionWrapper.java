package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Interfaces.ThrowingFunctionWithoutArgs;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class ExceptionWrapper {

    protected ArrayList<Function<Exception, String>> onErrorOutputs = new ArrayList<>();
    protected ArrayList<Callable<String>> onSuccessOutputs = new ArrayList<>();
    protected ThrowingFunctionWithoutArgs tf;

    public ExceptionWrapper(ThrowingFunctionWithoutArgs tf) {
        this.tf = tf;
    }

    public ExceptionWrapper outputOnError(Function<Exception, String> callable) {
        this.onErrorOutputs.add(callable);

        return this;
    }

    public ExceptionWrapper outputOnSuccess(Callable<String> callable) {
        this.onSuccessOutputs.add(callable);

        return this;
    }

    public void run() {
        try {

        } catch(Exception e) {

        }
    }

}
