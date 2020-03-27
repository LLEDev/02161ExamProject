package dk.dtu.SoftEngExamProjectG18.Core;

public class Employee {

    protected String ID;
    protected String name;
    protected int weeklyActivityCap = 10;

    public Employee(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Employee(String ID, String name, int weeklyActivityCap) {
        this.ID = ID;
        this.name = name;
        this.weeklyActivityCap = weeklyActivityCap;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return this.name;
    }

    public int getWeeklyActivityCap() {
        return weeklyActivityCap;
    }

    public void setWeeklyActivityCap(int weeklyActivityCap) {
        this.weeklyActivityCap = weeklyActivityCap;
    }

}
