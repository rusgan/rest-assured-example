package org.sora.dataproviders.db;

import java.sql.*;

import static org.sora.properties.Properties.DB_PORT;
import static org.sora.properties.Properties.DB_URL;
import static org.assertj.core.api.Assertions.assertThat;

public class Connections {

    public static Connection createCon(String db, String pass){

        String databaseURL = "jdbc:postgresql://" + DB_URL + ":" + DB_PORT + "/" + db;

        Connection connection = null;
        try {
            System.out.println("Connecting to Database...");
            connection = DriverManager
                    .getConnection(databaseURL, db, pass);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println(String.format("Failed to make connection to %s database", db));
        }

        assertThat(connection).as("Failed to make connection to %s database", db).isNotNull();
        return connection;
    }

    public static void close(Connection connection){
        if (connection != null) {
            try {
                System.out.println("Closing Database Connection...");
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}