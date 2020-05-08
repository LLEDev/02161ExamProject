package dk.dtu.SoftEngExamProjectG18.tests.Util;

import dk.dtu.SoftEngExamProjectG18.Business.Project;

public class TestHolder {

    private static TestHolder instance;

    /**
     * @author Lasse Lund-Egmose (s194568)
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
     * @author Lasse Lund-Egmose (s194568)
     */
    public Project getProject() {
        return this.project;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public CmdResponse getResponse() {
        return this.response;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void setResponse(CmdResponse response) {
        this.response = response;
    }

}