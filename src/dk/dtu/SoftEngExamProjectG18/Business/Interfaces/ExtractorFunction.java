package dk.dtu.SoftEngExamProjectG18.Business.Interfaces;

import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.ExtractionException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Someone
 */
@FunctionalInterface
public interface ExtractorFunction {
    ArrayList<HashMap<String, String>> get() throws ExtractionException;
}
