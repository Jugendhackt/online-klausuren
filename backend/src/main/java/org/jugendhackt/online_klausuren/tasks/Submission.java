package org.jugendhackt.online_klausuren.tasks;

public class Submission {

    public final Task task;
    public final String solution;

    public Submission(Task task, String solution) {
        this.task = task;
        this.solution = solution;
    }
}
