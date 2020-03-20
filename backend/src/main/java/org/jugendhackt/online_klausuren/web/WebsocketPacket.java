package org.jugendhackt.online_klausuren.web;

import com.google.gson.JsonObject;

public class WebsocketPacket {
    private String function;
    private JsonObject data;

    public WebsocketPacket(String function, JsonObject data) {
        this.function = function;
        this.data = data;
    }

    public String getFunction() {
        return function;
    }

    public JsonObject getData() {
        return data;
    }
}
