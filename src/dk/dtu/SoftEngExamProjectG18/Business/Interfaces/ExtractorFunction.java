package dk.dtu.SoftEngExamProjectG18.Business.Interfaces;

import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.ExtractionException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Lasse Lund-Egmose (s194568)
 */
@FunctionalInterface
public interface ExtractorFunction {
    ArrayList<HashMap<String, String>> get() throws ExtractionException;
}
