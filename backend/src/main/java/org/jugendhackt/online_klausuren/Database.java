package org.jugendhackt.online_klausuren;

import org.jugendhackt.online_klausuren.tasks.MultipleChoiceTask;
import org.jugendhackt.online_klausuren.tasks.Task;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class Database {

    private Connection connection;

    public Database() {
        try {
            File file = new File("data.db");
            boolean isCreated = file.exists();
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            if (!isCreated) createSchemes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        reloadTests();
    }

    private void createSchemes() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS auth (token VARCHAR(30) PRIMARY KEY, test VARCHAR(36), name VARCHAR(10))");
        statement.execute("CREATE TABLE IF NOT EXISTS taken_tests (data TEXT, test VARCHAR(36))");
        statement.execute("CREATE TABLE IF NOT EXISTS tests (id VARCHAR(36) PRIMARY KEY, tasks TEXT)");
    }

    private void reloadTests() {
        //TODO LOAD! (Nicht einfach hardcoded)
        GLOBAL_VARS.tests.add(new Test(UUID.randomUUID(), new Task[]{new MultipleChoiceTask("Warum ist die Banane krumm?", "", 60, new HashMap<String, String>(){{put("0", "Wegen der Sonne"); put("1", "Wegen den Affen");}}, UUID.randomUUID())}));
    }

    public void saveStudent(Test test, Student student) {
        String data = GLOBAL_VARS.gson.toJson(student);
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO taken_tests (test, data) VALUES (?, ?)");
            statement.setString(1, test.getUuid().toString());
            statement.setString(2, data);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Test getTestForAuthToken(String token) {
        //TODO Load token from Database check Test UUID for that token and load with function GLOBAL_VARS getTestForUUID
        return null;
    }

    /**
     * Get a auth token for a new User
     * @param name of the student
     * @return auth token
     */
    public String createUser(String name, Test test) {
        //TODO
        return null;
    }

}
