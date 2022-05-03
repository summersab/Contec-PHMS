package de.greenrobot.event.util;

public class ThrowableFailureEvent {
    protected final boolean suppressErrorUi;
    protected final Throwable throwable;

    public ThrowableFailureEvent(Throwable throwable2) {
        this.throwable = throwable2;
        this.suppressErrorUi = false;
    }

    public ThrowableFailureEvent(Throwable throwable2, boolean suppressErrorUi2) {
        this.throwable = throwable2;
        this.suppressErrorUi = suppressErrorUi2;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public boolean isSuppressErrorUi() {
        return this.suppressErrorUi;
    }
}
