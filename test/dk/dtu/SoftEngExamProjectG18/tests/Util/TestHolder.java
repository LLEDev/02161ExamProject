package dk.dtu.SoftEngExamProjectG18.tests.Util;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Business.Project;

public class TestHolder {

    private static TestHolder instance;

    public static TestHolder getInstance() {
        if (instance == null) {
            instance = new TestHolder();
        }

        return instance;
    }

    protected Application application = Application.getInstance();
    protected CmdResponse response;
    protected Project project;

    public Application getApplication() {
        return this.application;
    }

    public Project getProject() {
        return this.project;
    }

    public CmdResponse getResponse() {
        return this.response;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setResponse(CmdResponse response) {
        this.response = response;
    }

}