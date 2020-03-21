package org.jugendhackt.online_klausuren;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
        statement.execute("CREATE TABLE IF NOT EXISTS tests (id VARCHAR(36) PRIMARY KEY, tasks TEXT, archived INTEGER DEFAULT 0)");
    }

    private void reloadTests() {
        try {
            Statement statement = connection.createStatement();
            ResultSet tests = statement.executeQuery("SELECT id, tasks FROM tests WHERE archived = 0");
            while (tests.next()) {
                JsonArray rawTasks = GLOBAL_VARS.gson.fromJson(tests.getString("tasks"), JsonArray.class);
                Task[] tasks = new Task[rawTasks.size()];
                for (int i = 0; i < rawTasks.size(); i++) {
                    JsonObject task = rawTasks.get(i).getAsJsonObject();
                    String title = task.get("title").getAsString();
                    String description = task.get("description").getAsString();
                    int time = task.get("time").getAsInt();
                    UUID id = UUID.fromString(task.get("id").getAsString());
                    if (task.get("type").getAsString().equals("CHOICES")) {
                        tasks[i] = new MultipleChoiceTask(title, description, time, GLOBAL_VARS.gson.fromJson(GLOBAL_VARS.gson.toJson(task.get("choices")), new HashMap<String, String>().getClass()), id);
                    }
                }
                GLOBAL_VARS.tests.add(new Test(UUID.fromString(tests.getString("id")), tasks));
            }
            tests.close();
            } catch (SQLException e) {
            e.printStackTrace();
        }
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
