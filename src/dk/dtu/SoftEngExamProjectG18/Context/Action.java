package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Interfaces.ThrowingFunction;

import java.util.ArrayList;

public class Action {

    protected String[] arguments;
    protected ThrowingFunction<String[]> tf;
    protected String signature;

    public Action(String signature, String[] arguments, ThrowingFunction<String[]> tf) {
        this.signature = signature;
        this.arguments = arguments;
        this.tf = tf;
    }

    public String[] getArguments() {
        return this.arguments;
    }

    public String getFullSignature() {
        if(this.getArguments().length == 0) {
            return this.getSignature();
        }

        return this.getSignature() + " {" + String.join("} {", this.getArguments()) + "}";
    }

    public String getSignature() {
        return this.signature;
    }

    public void run(String[] args) throws Exception {
        this.tf.apply(args);
    }

}
