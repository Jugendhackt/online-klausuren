package org.jugendhackt.online_klausuren.tasks;

import java.util.UUID;

public class Submission {

    public final UUID task;
    public final String value;

    public Submission(Task task, String value) {
        this.task = task.id;
        this.value = value;
    }
}
