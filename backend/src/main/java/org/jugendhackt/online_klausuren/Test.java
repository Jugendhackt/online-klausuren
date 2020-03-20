package org.jugendhackt.online_klausuren;


import org.jugendhackt.online_klausuren.tasks.Submission;
import org.jugendhackt.online_klausuren.tasks.Task;
import org.jugendhackt.online_klausuren.web.WebSocket;
import org.jugendhackt.online_klausuren.web.WebsocketPacket;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {

    private List<Student> students;
    private Task[] tasks;

    public Test(Task[] tasks) {
        students = new ArrayList<>();
        this.tasks = tasks;
    }

    public void addStudent(Session session) {
        Student student = new Student(session, Util.shuffle(tasks));
        students.add(student);

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
            WebSocket.sendMessage(session, GLOBAL_VARS.gson.toJson(new WebsocketPacket("task", GLOBAL_VARS.gson.toJsonTree(nextTask).getAsJsonObject())));
        } else {
            //TODO Send end packet
        }
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

}
