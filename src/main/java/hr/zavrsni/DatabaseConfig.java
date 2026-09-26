package hr.zavrsni;

public final class DatabaseConfig {
    private DatabaseConfig() {}

    // PRILAGODI OVE VRIJEDNOSTI SVOM SQL SERVERU.
    public static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=JavaAdv;encrypt=true;trustServerCertificate=true";
    public static final String USER = "sa";
    public static final String PASSWORD = "SQL";
}
