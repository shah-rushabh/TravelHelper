
package java_project;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;

import com.mysql.jdbc.ResultSetMetaData;

/**
 * The example is modified from the example by Prof. Engle.
 * This class demonstrates how to access your MySql database from the Java program using JDBC.
 * You need to have a database.properties file with username, password, database, and hostname in the project
 * directory.
 * You must also have the tunnel to stargate.cs.usfca.edu running if you are
 * off-campus.
 */
public class SimpleJDBCExample {

    /**
     * URI to use when connecting to database. Should be in the format:
     * jdbc:subprotocol://hostname/database
     */
    public String uri = "";

    /**
     * Attempts to load properties file with database configuration. Must
     * include username, password, database, and hostname.
     *
     * @param configPath path to database properties file
     * @return database properties
     * @throws IOException           if unable to properly parse properties file
     * @throws FileNotFoundException if properties file not found
     */
    private Properties loadConfig(String configPath) throws FileNotFoundException, IOException {

        // Load properties file
        Properties config = new Properties();
        config.load(new FileReader(configPath));

        return config;
    }
    /**
     * Connect to the database and send a simple query. Print the results.
     */
    public void connectToDatabase() {
        try {

            Properties config = loadConfig("database.properties");

            // Create database URI in proper format
            String uri = String.format("jdbc:mysql://%s/%s", config.getProperty("hostname"),
                    config.getProperty("database"));
            System.out.println("uri = " + uri);

            PreparedStatement sql; // prepared statement
            try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {

                sql = dbConnection.prepareStatement("select * from customers where cust_id = 10001");

                ResultSet results = sql.executeQuery();
                ResultSetMetaData rsmd = (ResultSetMetaData) results.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (results.next()) { // go along rows using the iterator
                    // iterate along columns
                    for (int i = 0; i < columnsNumber; i++)
                        System.out.print(results.getString(i + 1) + " ");
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.err.println("Unable to connect properly to database.");
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        SimpleJDBCExample test = new SimpleJDBCExample();
        test.connectToDatabase();
    }
}
