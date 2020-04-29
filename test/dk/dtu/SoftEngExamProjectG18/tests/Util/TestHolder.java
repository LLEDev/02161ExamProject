package dk.dtu.SoftEngExamProjectG18.tests.Util;

import dk.dtu.SoftEngExamProjectG18.Business.Project;

public class TestHolder {

    private static TestHolder instance;

    public CmdResponse response;
    public Project project;

    public static TestHolder getInstance() {
        if (instance == null) {
            instance = new TestHolder();
        }

        return instance;
    }

}