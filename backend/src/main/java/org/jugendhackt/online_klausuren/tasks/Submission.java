package org.jugendhackt.online_klausuren.tasks;

public class Submission {

    public final Task task;
    public final String value;

    public Submission(Task task, String value) {
        this.task = task;
        this.value = value;
    }
}
