package org.jugendhackt.online_klausuren;

import org.jugendhackt.online_klausuren.tasks.MultipleChoiceTask;
import org.jugendhackt.online_klausuren.tasks.Task;

import java.util.HashMap;
import java.util.UUID;

public class Database {

    public Database() {
        reloadTests();
    }

    private void reloadTests() {
        //TODO LOAD! (Nicht einfach hardcoded)
        GLOBAL_VARS.tests.add(new Test(UUID.randomUUID(), new Task[]{new MultipleChoiceTask("Warum ist die Banane krumm?", "", 60, new HashMap<String, String>(){{put("0", "Wegen der Sonne"); put("1", "Wegen den Affen");}})}));
    }

    public void saveStudent(Test test, Student student) {
        String data = GLOBAL_VARS.gson.toJson(student);
        //TODO SAVE
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
