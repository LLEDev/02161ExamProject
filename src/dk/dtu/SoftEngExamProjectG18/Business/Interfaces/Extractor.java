package dk.dtu.SoftEngExamProjectG18.Business.Interfaces;

import java.util.ArrayList;
import java.util.HashMap;

public interface Extractor<T> {

    ArrayList<HashMap<String, String>> extract(ArrayList<T> collection, HashMap<String, Object> metaData) throws IllegalArgumentException;

}
