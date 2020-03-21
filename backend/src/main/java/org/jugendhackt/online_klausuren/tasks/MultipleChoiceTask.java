package org.jugendhackt.online_klausuren.tasks;

import java.util.Map;
import java.util.UUID;

public class MultipleChoiceTask extends Task {
    public final Map<String, String> choices;

    public MultipleChoiceTask(String title, String description, int time, Map<String, String> choices, UUID id) {
        super(title, description, time, id);
        this.choices = choices;
        this.type = "CHOICES";
    }
}
