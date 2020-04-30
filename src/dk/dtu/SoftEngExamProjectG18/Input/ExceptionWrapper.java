package dk.dtu.SoftEngExamProjectG18.Input;

import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.AccessDeniedException;
import dk.dtu.SoftEngExamProjectG18.Input.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.General.Interfaces.ThrowingFunctionWithoutArgs;
import dk.dtu.SoftEngExamProjectG18.General.Interfaces.ZeroArgumentFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExceptionWrapper {

    protected static final Class[] CHECKED_EXCEPTIONS = new Class[] {
        AccessDeniedException.class,
        IllegalArgumentException.class,
    };

    protected ArrayList<Consumer<Exception>> exceptionHooks;
    protected InputContext context;
    protected ArrayList<Function<CommandException, String>> onErrorOutputs = new ArrayList<>();
    protected ArrayList<ZeroArgumentFunction<String>> onSuccessOutputs = new ArrayList<>();
    protected ThrowingFunctionWithoutArgs tf;

    public ExceptionWrapper(
        InputContext context,
        ThrowingFunctionWithoutArgs tf,
        ArrayList<Consumer<Exception>> exceptionHooks
    ) {
        this.exceptionHooks = exceptionHooks;
        this.context = context;
        this.tf = tf;
    }

    public ExceptionWrapper outputOnError(Function<CommandException, String> callable) {
        this.onErrorOutputs.add(callable);

        return this;
    }

    public ExceptionWrapper outputOnSuccess(ZeroArgumentFunction<String> callable) {
        this.onSuccessOutputs.add(callable);

        return this;
    }

    public void run() {
        try {
            this.tf.apply();
            this.onSuccessOutputs.forEach(consumer -> this.context.writeOutput(consumer.apply()));
        } catch (Exception e) {
            if(Arrays.asList(CHECKED_EXCEPTIONS).contains(e.getClass())) {
                CommandException ce = new CommandException(e.getMessage());
                this.onErrorOutputs.forEach(consumer -> this.context.writeOutput(consumer.apply(ce)));
                this.exceptionHooks.forEach(consumer -> consumer.accept(ce));
            } else {
                this.exceptionHooks.forEach(consumer -> consumer.accept(e));
            }
        }
    }

}
