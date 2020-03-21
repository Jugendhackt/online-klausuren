package org.jugendhackt.online_klausuren;


import com.google.gson.JsonObject;
import org.jugendhackt.online_klausuren.tasks.Submission;
import org.jugendhackt.online_klausuren.tasks.Task;
import org.jugendhackt.online_klausuren.web.WebSocket;
import org.jugendhackt.online_klausuren.web.WebsocketPacket;

import javax.websocket.Session;
import java.util.*;

public class Test {

    private List<Student> students;
    private Task[] tasks;
    private final UUID uuid;

    public Test(UUID uuid, Task[] tasks) {
        students = new ArrayList<>();
        this.tasks = tasks;
        this.uuid = uuid;
    }

    public void addStudent(Session session) {
        Student student = new Student(session, Util.shuffle(tasks));
        students.add(student);
        student.setCurrentTask(student.getTaskList()[0]);
        sendTaskToStudent(student);
    }

    public void submissionSent(Session session, String submission, String taskID) {
        Student student = getStudentBySession(session);
        Task task = getTaskByID(taskID);
        if (task == null || student == null || task != student.getCurrentTask()) {
            //z.B. WebSocket.sendMessage(session, "{\"error\": \"Task not valid\"}");
            return;
        }
        student.addSubmission(new Submission(task, submission));
        Task nextTask = getNextTaskForStudent(student);
        if (nextTask != null) {
            student.setCurrentTask(nextTask);
            sendTaskToStudent(student);
        } else {
            //TODO Send end packet
        }
    }

    private void sendTaskToStudent(Student student) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("task", GLOBAL_VARS.gson.toJsonTree(student.getCurrentTask()).getAsJsonObject());
        //TODO add Deadline
        WebSocket.sendMessage(student.getSession(), GLOBAL_VARS.gson.toJson(new WebsocketPacket("task", jsonObject)));
    }

    private Task getNextTaskForStudent(Student student) {
        for (int i = 0; i < student.getTaskList().length - 1; i++) {
            if (student.getTaskList()[i] == student.getCurrentTask()) return student.getTaskList()[i+1];
        }
        return null;
    }

    Student getStudentBySession(Session session) {
        for (Student student : students) if (student.getSession() == session) return student;
        return null;
    }

    private Task getTaskByID(String taskID) {
        for (Task task : tasks) if (task.id.toString().equals(taskID)) return task;
        return null;
    }

    public UUID getUuid() {
        return uuid;
    }
}