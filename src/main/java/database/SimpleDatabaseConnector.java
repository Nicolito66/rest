package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SimpleDatabaseConnector {
    private static String username = "root";
    private static String password = "HelloWorld99!";
    private static String url = "jdbc:mysql://localhost:3306/db";
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    private String request;
    private ResultSet result;

    public SimpleDatabaseConnector() {

    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Une erreur est survenue lors de la connexion à la base de donnée.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet execute() throws SQLException {
        Statement statement = connection.createStatement();
        result = statement.executeQuery(request);
        return result;
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public List<String> getColumnResultAsList() throws SQLException {
        List<String> resultList = new ArrayList<>();
        if (result != null) {
            while (result.next()) {
                resultList.add(result.getString(0));
            }
        }
        return resultList;
    }

}
