package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;

import java.util.Date;

public class Project {

    protected Date createdAt;
    protected int ID;
    protected boolean isBillable = true;
    protected String name;
    protected Employee PM = null;

    protected void setupID() {
        this.ID = CompanyDB.getInstance().incrementNextProjectID(this.createdAt.getYear());
    }

    public Project(String name) {
        this.name = name;

        this.setupID();
    }

    public Project(String name, boolean isBillable) {
        this.isBillable = isBillable;
        this.name = name;

        this.setupID();
    }

    public Project(String name, Date createdAt, boolean isBillable) {
        this.createdAt = createdAt;
        this.isBillable = isBillable;
        this.name = name;

        this.setupID();
    }

    public Project(String name, Date createdAt, boolean isBillable, Employee PM) {
        this.createdAt = createdAt;
        this.isBillable = isBillable;
        this.name = name;
        this.PM = PM;

        this.setupID();
    }

    public String getID() {
        return this.createdAt.getYear() + "-" + this.ID;
    }


}
