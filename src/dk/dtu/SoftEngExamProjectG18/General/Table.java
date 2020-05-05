package dk.dtu.SoftEngExamProjectG18.General;

import dk.dtu.SoftEngExamProjectG18.Business.Interfaces.Extractable;

import java.util.ArrayList;
import java.util.HashMap;

public class Table {

    protected static HashMap<String, Integer> determineColumnWidths(String[] keyOrder, ArrayList<HashMap<String, String>> data) {
        HashMap<String, Integer> columnWidths = new HashMap<>();

        for(String key : keyOrder) {
            columnWidths.put(key, key.length() + 2);
        }

        for(HashMap<String, String> entry : data) {
            for(String key : keyOrder) {
                if(entry.containsKey(key)) {
                    int keyLength = (entry.get(key) != null ? entry.get(key).length() : 0) + 2;

                    if (keyLength > columnWidths.get(key)) {
                        columnWidths.put(key, keyLength);
                    }
                }
            }
        }

        return columnWidths;
    }

    protected static String makeDelimiter(HashMap<String, Integer> columnWidths) {
        int numChars = columnWidths.values().stream().mapToInt(i -> i).sum() + columnWidths.size() + 1;

        StringBuilder sb = new StringBuilder();
        while(sb.length() < numChars) {
            sb.append("-");
        }

        return sb.toString();
    }

    protected static String makeTitleRow(String[] keyOrder, HashMap<String, Integer> columnWidths) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");

        for(String key : keyOrder) {
            sb.append(padToWidth(key, columnWidths.get(key))).append("|");
        }

        return sb.toString();
    }

    protected static String makeRow(String[] keyOrder, HashMap<String, String> entry, HashMap<String, Integer> columnWidths) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");

        for(String key : keyOrder) {
            String value = entry.containsKey(key) && entry.get(key) != null ? entry.get(key) : "";
            sb.append(padToWidth(value, columnWidths.get(key))).append("|");
        }

        return sb.toString();
    }

    protected static String padToWidth(String str, int width) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(str);

        while(sb.length() < width) {
            sb.append(" ");
        }

        return sb.toString();
    }

    public static String make(String context, String[] keyOrder, HashMap<String, Object> metaData, ArrayList<? extends Extractable<?>> collection) {
        String result = "No data found.";

        if(collection.size() > 0) {
            ArrayList<HashMap<String, String>> data = collection.get(0).extract(context, metaData, collection);

            HashMap<String, Integer> columnWidths = determineColumnWidths(keyOrder, data);
            String delimiter = makeDelimiter(columnWidths);

            StringBuilder resultBuilder = new StringBuilder();
            resultBuilder.append(delimiter)
                .append("\n").append(makeTitleRow(keyOrder, columnWidths))
                .append("\n").append(delimiter);

            for (HashMap<String, String> entry : data) {
                resultBuilder.append("\n").append(makeRow(keyOrder, entry, columnWidths))
                    .append("\n").append(delimiter);
            }

            result = resultBuilder.toString();
        }

        return result;
    }

    public static String make(String context, String[] keyOrder, ArrayList<? extends Extractable<?>> collection) {
        return make(context, keyOrder, new HashMap<>(), collection);
    }

}
