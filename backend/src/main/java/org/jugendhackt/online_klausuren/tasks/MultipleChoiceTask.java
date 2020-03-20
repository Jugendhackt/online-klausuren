package org.jugendhackt.online_klausuren.tasks;

import java.util.Map;

public class MultipleChoiceTask extends Task {
    public final Map<Integer, String> choices;

    public MultipleChoiceTask(String title, String description, int time, Map<Integer, String> choices) {
        super(title, description, time);
        this.choices = choices;
    }
}
