package dk.dtu.SoftEngExamProjectG18.General.Interfaces;

/**
 * @author Lasse Lund-Egmose (s194568)
 */
@FunctionalInterface
public interface ThrowingFunction<T> {
    void apply(T arg) throws Exception;
}