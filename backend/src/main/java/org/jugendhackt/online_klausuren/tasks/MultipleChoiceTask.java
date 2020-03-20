package org.jugendhackt.online_klausuren.tasks;

import java.util.Map;

public class MultipleChoiceTask extends Task {
    public final Map<String, String> choices;

    public MultipleChoiceTask(String title, String description, int time, Map<String, String> choices) {
        super(title, description, time);
        this.choices = choices;
        this.type = "CHOICES";
    }
}
