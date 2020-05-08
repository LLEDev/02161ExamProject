package dk.dtu.SoftEngExamProjectG18.Controller;

import dk.dtu.SoftEngExamProjectG18.General.Interfaces.ThrowingFunction;

public class Action {

    protected String[] arguments;
    protected ThrowingFunction<String[]> tf;
    protected String signature;

    /**
     * @author Someone
     */
    public Action(String signature, String[] arguments, ThrowingFunction<String[]> tf) {
        this.signature = signature;
        this.arguments = arguments;
        this.tf = tf;
    }

    /**
     * @author Someone
     */
    public String[] getArguments() {
        return this.arguments;
    }

    /**
     * @author Someone
     */
    public ThrowingFunction<String[]> getFunction() {
        return this.tf;
    }

    /**
     * @author Someone
     */
    public String getFullSignature() {
        if (this.getArguments().length == 0) {
            return this.getSignature();
        }

        return this.getSignature() + " {" + String.join("} {", this.getArguments()) + "}";
    }

    /**
     * @author Someone
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * @author Someone
     */
    public void run(String[] args) throws Exception {
        this.tf.apply(args);
    }

}
