// ScreenEvent.java
package com.mrbreaknfix.event.events;

import com.mrbreaknfix.event.EventStage;

public class ScreenEvent extends EventStage {
    // BEFORE_INIT, AFTER_INIT events
    public static class BeforeInit extends ScreenEvent {
        private static final BeforeInit INSTANCE = new BeforeInit();

        public static BeforeInit get() {
            return INSTANCE;
        }
    }

    public static class AfterInit extends ScreenEvent {
        private static final AfterInit INSTANCE = new AfterInit();

        public static AfterInit get() {
            return INSTANCE;
        }
    }

    public static class Close extends ScreenEvent {
        private static final Close INSTANCE = new Close();

        public static Close get() {
            return INSTANCE;
        }
    }
}
