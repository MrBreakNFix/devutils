package com.mrbreaknfix.event.events;

import com.mrbreaknfix.event.EventStage;

public class ChatMessageEvent extends EventStage {
    private final String message;

    public ChatMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
