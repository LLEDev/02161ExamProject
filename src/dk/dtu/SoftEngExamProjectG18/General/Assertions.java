package dk.dtu.SoftEngExamProjectG18.General;

import java.util.function.Supplier;

public class Assertions {

    /**
     * @author Someone
     */
    public static <X extends Throwable> void assertOrThrow(Supplier<? extends X> exceptionSupplier, boolean statement) throws X {
        X throwable = exceptionSupplier.get();

        if (!statement) {
            throw throwable;
        }
    }


}
