package dk.dtu.SoftEngExamProjectG18.Interface;

import java.util.ArrayList;
import java.util.HashMap;

public interface Extractable<T extends Extractable<T>> {

    ArrayList<HashMap<String, String>> extract(String context, ArrayList<? extends Extractable<?>> collection);

}
