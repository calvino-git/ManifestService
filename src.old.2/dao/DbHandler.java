package dao;

import static base.Const.LOG;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import properties.Config;
public class DbHandler {
    private static Connection dbConnection;
    public static Connection getDbConnection() {
        try {
            if(dbConnection == null || dbConnection.isClosed()) setDbConnection();
        } catch (SQLException ex) {
            LOG.info(ex.getMessage());
        }
        return dbConnection;
    }

    
    private static void setDbConnection() {
        String host = Config.PROPERTIES.getString("cargo.host");
        String port = Config.PROPERTIES.getString("cargo.port");
        String dbname = Config.PROPERTIES.getString("cargo.sid");
        String user = Config.PROPERTIES.getString("cargo.user");
        String password = Config.PROPERTIES.getString("cargo.password");
        final String ConnectionString = Config.PROPERTIES.getString("cargo.url") + host + ":" + port + ":" + dbname;
        try {
            Class.forName(Config.PROPERTIES.getString("cargo.driver"));
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(ConnectionString, user, password);

        } catch (SQLException e) {
            LOG.info(e.getMessage());
        }
    }

}
