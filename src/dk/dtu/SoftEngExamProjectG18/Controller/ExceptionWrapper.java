package dk.dtu.SoftEngExamProjectG18.Controller;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.AccessDeniedException;
import dk.dtu.SoftEngExamProjectG18.Controller.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.General.Interfaces.ThrowingFunctionWithoutArgs;
import dk.dtu.SoftEngExamProjectG18.General.Interfaces.ZeroArgumentFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExceptionWrapper {

    protected static final Class[] CHECKED_EXCEPTIONS = new Class[]{
        AccessDeniedException.class,
        IllegalArgumentException.class,
    };

    protected ArrayList<Consumer<Exception>> exceptionHooks = new ArrayList<>();
    protected ArrayList<Function<CommandException, String>> onErrorOutputs = new ArrayList<>();
    protected ArrayList<ZeroArgumentFunction<String>> onSuccessOutputs = new ArrayList<>();
    protected boolean sandbox = false;
    protected ThrowingFunctionWithoutArgs tf;

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public ExceptionWrapper(ThrowingFunctionWithoutArgs tf) {
        this.tf = tf;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public ExceptionWrapper(ThrowingFunctionWithoutArgs tf, ArrayList<Consumer<Exception>> exceptionHooks, boolean sandbox) {
        this(tf);
        this.exceptionHooks = exceptionHooks;
        this.sandbox = sandbox;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public ExceptionWrapper outputOnError(Function<CommandException, String> callable) {
        this.onErrorOutputs.add(callable);

        return this;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public ExceptionWrapper outputOnSuccess(ZeroArgumentFunction<String> callable) {
        this.onSuccessOutputs.add(callable);

        return this;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void run() {
        // Do not run business logic if testing UI
        if (this.sandbox) {
            return;
        }

        InputContext context = Application.getInstance().getContext();

        try {
            this.tf.apply();
            this.onSuccessOutputs.forEach(consumer -> context.writeOutput(consumer.apply()));
        } catch (Exception e) {
            CommandException ce = null;

            if (Arrays.asList(CHECKED_EXCEPTIONS).contains(e.getClass())) {
                ce = new CommandException(e.getMessage());

                final CommandException fce = ce;
                this.onErrorOutputs.forEach(consumer -> context.writeOutput(consumer.apply(fce)));
            }

            final Exception fe = ce == null ? e : ce;
            this.exceptionHooks.forEach(consumer -> consumer.accept(fe));
        }
    }

}
