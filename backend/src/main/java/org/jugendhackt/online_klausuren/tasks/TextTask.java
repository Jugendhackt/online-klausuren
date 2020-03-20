package org.jugendhackt.online_klausuren.tasks;

public class TextTask extends Task{

    public TextTask(String title, String description, int time) {
        super(title, description, time);
        this.type = "TEXT";
    }
}
