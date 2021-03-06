package dk.dtu.SoftEngExamProjectG18.General;

import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.ExtractionException;
import dk.dtu.SoftEngExamProjectG18.Business.Interfaces.ExtractorFunction;

import java.util.ArrayList;
import java.util.HashMap;

public class Table {

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static HashMap<String, Integer> determineColumnWidths(String[] keyOrder, ArrayList<HashMap<String, String>> data) {
        HashMap<String, Integer> columnWidths = new HashMap<>();

        for (String key : keyOrder) {
            columnWidths.put(key, key.length() + 2);
        }

        for (HashMap<String, String> entry : data) {
            for (String key : keyOrder) {
                if (entry.containsKey(key)) {
                    int keyLength = (entry.get(key) != null ? entry.get(key).length() : 0) + 2;

                    if (keyLength > columnWidths.get(key)) {
                        columnWidths.put(key, keyLength);
                    }
                }
            }
        }

        return columnWidths;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static String makeDelimiter(HashMap<String, Integer> columnWidths) {
        int numChars = columnWidths.values().stream().mapToInt(i -> i).sum() + columnWidths.size() + 1;

        StringBuilder sb = new StringBuilder();
        while (sb.length() < numChars) {
            sb.append("-");
        }

        return sb.toString();
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static String makeTitleRow(String[] keyOrder, HashMap<String, Integer> columnWidths) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");

        for (String key : keyOrder) {
            sb.append(padToWidth(key, columnWidths.get(key))).append("|");
        }

        return sb.toString();
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static String makeRow(String[] keyOrder, HashMap<String, String> entry, HashMap<String, Integer> columnWidths) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");

        for (String key : keyOrder) {
            String value = entry.containsKey(key) && entry.get(key) != null ? entry.get(key) : "";
            sb.append(padToWidth(value, columnWidths.get(key))).append("|");
        }

        return sb.toString();
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    protected static String padToWidth(String str, int width) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(str);

        while (sb.length() < width) {
            sb.append(" ");
        }

        return sb.toString();
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static String make(ExtractorFunction dataExtractor, String[] keyOrder) {
        String result = "No data found.";

        ArrayList<HashMap<String, String>> extractedData;

        try {
            extractedData = dataExtractor.get();
        } catch (ExtractionException e) {
            return "An error occurred: " + e.getMessage();
        }

        if (extractedData.size() > 0) {
            HashMap<String, Integer> columnWidths = determineColumnWidths(keyOrder, extractedData);
            String delimiter = makeDelimiter(columnWidths);

            StringBuilder resultBuilder = new StringBuilder();
            resultBuilder.append(delimiter)
                .append("\n").append(makeTitleRow(keyOrder, columnWidths))
                .append("\n").append(delimiter);

            for (HashMap<String, String> entry : extractedData) {
                resultBuilder.append("\n").append(makeRow(keyOrder, entry, columnWidths))
                    .append("\n").append(delimiter);
            }

            result = resultBuilder.toString();
        }

        return result;
    }

}
