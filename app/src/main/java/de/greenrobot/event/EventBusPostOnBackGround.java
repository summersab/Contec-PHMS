package de.greenrobot.event;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class EventBusPostOnBackGround {
    private EventBus mEventBus;
    private EventPostHandler mainPoster = new EventPostHandler(Looper.getMainLooper());

    public EventBusPostOnBackGround(EventBus pEventBus) {
        this.mEventBus = pEventBus;
    }

    public void postInMainThread(Object pEvent) {
        this.mainPoster.post(pEvent);
    }

    @SuppressLint({"HandlerLeak"})
    class EventPostHandler extends Handler {
        public EventPostHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            EventBusPostOnBackGround.this.mEventBus.post(msg.obj);
        }

        void post(Object event) {
            sendMessage(obtainMessage(0, event));
        }
    }
}
