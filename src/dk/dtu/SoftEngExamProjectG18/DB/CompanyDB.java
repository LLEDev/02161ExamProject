package dk.dtu.SoftEngExamProjectG18.DB;

public class CompanyDB {

    private static CompanyDB instance;

    private CompanyDB() {}

    public static CompanyDB getInstance() {
        if (instance == null) {
            instance = new CompanyDB();
        }

        return instance;
    }

}
