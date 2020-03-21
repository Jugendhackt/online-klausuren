package org.jugendhackt.online_klausuren.tasks;

import java.util.UUID;

public class TextTask extends Task{

    public TextTask(String title, String description, int time, UUID id) {
        super(title, description, time, id);
        this.type = "TEXT";
    }
}
