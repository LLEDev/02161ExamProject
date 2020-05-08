package dk.dtu.SoftEngExamProjectG18.Controller;

import java.util.HashMap;

public class ActionMap extends HashMap<String, Action> {

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static ActionMap build(Action[] actions) {
        ActionMap map = new ActionMap();

        for (Action a : actions) {
            map.put(a.getSignature(), a);
        }

        return map;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static ActionMap build(ActionMap inheritedMap, Action[] actions) {
        ActionMap map = (ActionMap) inheritedMap.clone();

        for (Action a : actions) {
            map.put(a.getSignature(), a);
        }

        return map;
    }

}
