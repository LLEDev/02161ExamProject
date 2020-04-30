package dk.dtu.SoftEngExamProjectG18.Business.Interfaces;

import java.util.ArrayList;
import java.util.HashMap;

public interface Extractable<T extends Extractable<T>> {

    ArrayList<HashMap<String, String>> extract(String context, HashMap<String, Object> metaData, ArrayList<? extends Extractable<?>> collection);

}
