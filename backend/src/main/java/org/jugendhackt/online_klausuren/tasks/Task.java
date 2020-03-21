package org.jugendhackt.online_klausuren.tasks;

import java.util.UUID;

public class Task {

    public final UUID id;
    public String type = "NONE";
    public final String title;
    public final String description;
    public final int time;

    public Task(String title, String description, int time, UUID id) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.id = id;
    }
}
