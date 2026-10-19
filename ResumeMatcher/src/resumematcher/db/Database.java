package resumematcher.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Creates JDBC connections using the settings in db.properties. */
public class Database {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = new FileInputStream("db.properties")) {
            PROPS.load(in);
        } catch (IOException e) {
            System.err.println("db.properties not found - using default settings");
        }
    }

    public static Connection get() throws SQLException {
        String url  = PROPS.getProperty("url", "jdbc:mysql://localhost:3306/resume_matcher");
        String user = PROPS.getProperty("user", "root");
        String pass = PROPS.getProperty("password", "");
        return DriverManager.getConnection(url, user, pass);
    }
}
