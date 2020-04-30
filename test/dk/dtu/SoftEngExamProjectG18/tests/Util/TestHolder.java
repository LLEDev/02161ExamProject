package dk.dtu.SoftEngExamProjectG18.tests.Util;

import dk.dtu.SoftEngExamProjectG18.Business.Project;

public class TestHolder {

    private static TestHolder instance;

    public static TestHolder getInstance() {
        if (instance == null) {
            instance = new TestHolder();
        }

        return instance;
    }

    protected CmdResponse response;
    protected Project project;

    public Project getProject() {
        return this.project;
    }

    public CmdResponse getResponse() {
        return this.response;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setResponse(CmdResponse response) {
        this.response = response;
    }

}