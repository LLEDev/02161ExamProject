package dk.dtu.SoftEngExamProjectG18.General.Interfaces;

@FunctionalInterface
public interface ThrowingFunction<T> {
    void apply(T arg) throws Exception;
}