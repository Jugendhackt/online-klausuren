package org.jugendhackt.online_klausuren.tasks;

public class TextTask extends Task{

    public final String solution;

    public TextTask(String title, String description, int time, String solution) {
        super(title, description, time);
        this.type = "TEXT";
        this.solution = solution;
    }
}
