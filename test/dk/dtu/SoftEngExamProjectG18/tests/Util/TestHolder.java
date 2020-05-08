package dk.dtu.SoftEngExamProjectG18.tests.Util;

import dk.dtu.SoftEngExamProjectG18.Business.Project;

public class TestHolder {

    private static TestHolder instance;

    /**
     * @author Someone
     */
    public static TestHolder getInstance() {
        if (instance == null) {
            instance = new TestHolder();
        }

        return instance;
    }

    protected CmdResponse response;
    protected Project project;

    /**
     * @author Someone
     */
    public Project getProject() {
        return this.project;
    }

    /**
     * @author Someone
     */
    public CmdResponse getResponse() {
        return this.response;
    }

    /**
     * @author Someone
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @author Someone
     */
    public void setResponse(CmdResponse response) {
        this.response = response;
    }

}