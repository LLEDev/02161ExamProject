package dk.dtu.SoftEngExamProjectG18.Controller;

import dk.dtu.SoftEngExamProjectG18.General.Interfaces.ThrowingFunction;

public class Action {

    protected String[] arguments;
    protected ThrowingFunction<String[]> tf;
    protected String signature;

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public Action(String signature, String[] arguments, ThrowingFunction<String[]> tf) {
        this.signature = signature;
        this.arguments = arguments;
        this.tf = tf;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public String[] getArguments() {
        return this.arguments;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public ThrowingFunction<String[]> getFunction() {
        return this.tf;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public String getFullSignature() {
        if (this.getArguments().length == 0) {
            return this.getSignature();
        }

        return this.getSignature() + " {" + String.join("} {", this.getArguments()) + "}";
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void run(String[] args) throws Exception {
        this.tf.apply(args);
    }

}
