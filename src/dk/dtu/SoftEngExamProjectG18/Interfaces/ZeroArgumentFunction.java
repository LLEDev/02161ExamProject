package dk.dtu.SoftEngExamProjectG18.Interfaces;

import java.util.function.Function;

@FunctionalInterface
public interface ZeroArgumentFunction<R> {
    R apply();
}
