package dk.dtu.SoftEngExamProjectG18.General.Interfaces;

/**
 * @author Lasse Lund-Egmose (s194568)
 */
@FunctionalInterface
public interface ThrowingFunctionWithoutArgs {
    void apply() throws Exception;
}
