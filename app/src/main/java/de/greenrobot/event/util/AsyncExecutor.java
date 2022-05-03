package de.greenrobot.event.util;

import android.util.Log;
import java.lang.reflect.Constructor;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import de.greenrobot.event.EventBus;

public class AsyncExecutor {
    private final EventBus eventBus;
    private final Constructor<?> failureEventConstructor;
    private final Executor threadPool;

    public interface RunnableEx {
        void run() throws Exception;
    }

    public static class Builder {
        private EventBus eventBus;
        private Class<?> failureEventType;
        private Executor threadPool;

        private Builder() {
        }

        /* synthetic */ Builder(Builder builder) {
            this();
        }

        public Builder threadPool(Executor threadPool2) {
            this.threadPool = threadPool2;
            return this;
        }

        public Builder failureEventType(Class<?> failureEventType2) {
            this.failureEventType = failureEventType2;
            return this;
        }

        public Builder eventBus(EventBus eventBus2) {
            this.eventBus = eventBus2;
            return this;
        }

        public AsyncExecutor build() {
            if (this.eventBus == null) {
                this.eventBus = EventBus.getDefault();
            }
            if (this.threadPool == null) {
                this.threadPool = Executors.newCachedThreadPool();
            }
            if (this.failureEventType == null) {
                this.failureEventType = ThrowableFailureEvent.class;
            }
            return new AsyncExecutor(this.threadPool, this.eventBus, this.failureEventType, (AsyncExecutor) null);
        }
    }

    public static Builder builder() {
        return new Builder((Builder) null);
    }

    public static AsyncExecutor create() {
        return new Builder((Builder) null).build();
    }

    private AsyncExecutor(Executor threadPool2, EventBus eventBus2, Class<?> failureEventType) {
        this.threadPool = threadPool2;
        this.eventBus = eventBus2;
        try {
            this.failureEventConstructor = failureEventType.getConstructor(new Class[]{Throwable.class});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failure event class must have a constructor with one parameter of type Throwable", e);
        }
    }

    /* synthetic */ AsyncExecutor(Executor executor, EventBus eventBus2, Class cls, AsyncExecutor asyncExecutor) {
        this(executor, eventBus2, cls);
    }

    public void execute(final RunnableEx runnable) {
        this.threadPool.execute(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    try {
                        AsyncExecutor.this.eventBus.post(AsyncExecutor.this.failureEventConstructor.newInstance(new Object[]{e}));
                    } catch (Exception e1) {
                        Log.e(EventBus.TAG, "Original exception:", e);
                        throw new RuntimeException("Could not create failure event", e1);
                    }
                }
            }
        });
    }
}
