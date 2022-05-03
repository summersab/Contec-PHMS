package de.greenrobot.event;

import android.util.Log;
import java.util.concurrent.CountDownLatch;

public class EventBusPostOnAys {
    private EventBus mEventBus;

    public EventBusPostOnAys(EventBus pEventBus) {
        this.mEventBus = pEventBus;
    }

    public void postOnAys(Object pEventToPost) throws InterruptedException {
        runThreadsSingleEventType(1, pEventToPost);
    }

    private void runThreadsSingleEventType(int threadCount, Object pEventToPost) throws InterruptedException {
        new PosterThread(new CountDownLatch(1), pEventToPost).start();
    }

    class PosterThread extends Thread {
        private final Object eventToPost;
        private final CountDownLatch startLatch;

        public PosterThread(CountDownLatch latch, Object eventToPost2) {
            this.startLatch = latch;
            this.eventToPost = eventToPost2;
        }

        public void run() {
            this.startLatch.countDown();
            try {
                this.startLatch.await();
            } catch (InterruptedException e) {
                Log.w(EventBus.TAG, "Unexpeced interrupt", e);
            }
            EventBusPostOnAys.this.mEventBus.post(this.eventToPost);
        }
    }
}
