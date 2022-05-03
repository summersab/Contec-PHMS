package de.greenrobot.event;

import android.os.Looper;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBus {
    private static /* synthetic */ int[] $SWITCH_TABLE$de$greenrobot$event$ThreadMode;
    public static String TAG = "Event";
    private static volatile EventBus defaultInstance;
    private static final Map<Class<?>, List<Class<?>>> eventTypesCache = new HashMap();
    static ExecutorService executorService = Executors.newCachedThreadPool();
    private final AsyncPoster asyncPoster = new AsyncPoster(this);
    private final BackgroundPoster backgroundPoster = new BackgroundPoster(this);
    private final ThreadLocal<List<Object>> currentThreadEventQueue = new ThreadLocal<List<Object>>() {
        protected List<Object> initialValue() {
            return new ArrayList();
        }
    };
    private final ThreadLocal<BooleanWrapper> currentThreadIsPosting = new ThreadLocal<BooleanWrapper>() {
        protected BooleanWrapper initialValue() {
            return new BooleanWrapper();
        }
    };
    private String defaultMethodName = "onEvent";
    private boolean logSubscriberExceptions = true;
    private final HandlerPoster mainThreadPoster = new HandlerPoster(this, Looper.getMainLooper(), 10);
    private final Map<Class<?>, Object> stickyEvents = new ConcurrentHashMap();
    private boolean subscribed;
    private final SubscriberMethodFinder subscriberMethodFinder = new SubscriberMethodFinder();
    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType = new HashMap();
    private final Map<Object, List<Class<?>>> typesBySubscriber = new HashMap();

    interface PostCallback {
        void onPostCompleted(List<SubscriberExceptionEvent> list);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$de$greenrobot$event$ThreadMode() {
        int[] iArr = $SWITCH_TABLE$de$greenrobot$event$ThreadMode;
        if (iArr == null) {
            iArr = new int[ThreadMode.values().length];
            try {
                iArr[ThreadMode.Async.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ThreadMode.BackgroundThread.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ThreadMode.MainThread.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ThreadMode.PostThread.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$de$greenrobot$event$ThreadMode = iArr;
        }
        return iArr;
    }

    public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

    public static void clearCaches() {
        SubscriberMethodFinder.clearCaches();
        eventTypesCache.clear();
    }

    public static void skipMethodNameVerificationFor(Class<?> clazz) {
        SubscriberMethodFinder.skipMethodNameVerificationFor(clazz);
    }

    public static void clearSkipMethodNameVerifications() {
        SubscriberMethodFinder.clearSkipMethodNameVerifications();
    }

    public void configureLogSubscriberExceptions(boolean logSubscriberExceptions2) {
        if (this.subscribed) {
            throw new EventBusException("This method must be called before any registration");
        }
        this.logSubscriberExceptions = logSubscriberExceptions2;
    }

    public void register(Object subscriber) {
        register(subscriber, this.defaultMethodName, false);
    }

    public void register(Object subscriber, String methodName) {
        register(subscriber, methodName, false);
    }

    public void registerSticky(Object subscriber) {
        register(subscriber, this.defaultMethodName, true);
    }

    public void registerSticky(Object subscriber, String methodName) {
        register(subscriber, methodName, true);
    }

    private void register(Object subscriber, String methodName, boolean sticky) {
        for (SubscriberMethod subscriberMethod : this.subscriberMethodFinder.findSubscriberMethods(subscriber.getClass(), methodName)) {
            subscribe(subscriber, subscriberMethod, sticky);
        }
    }

    public void register(Object subscriber, Class<?> eventType, Class<?>... moreEventTypes) {
        register(subscriber, this.defaultMethodName, false, eventType, moreEventTypes);
    }

    public synchronized void register(Object subscriber, String methodName, Class<?> eventType, Class<?>... moreEventTypes) {
        register(subscriber, methodName, false, eventType, moreEventTypes);
    }

    public void registerSticky(Object subscriber, Class<?> eventType, Class<?>... moreEventTypes) {
        register(subscriber, this.defaultMethodName, true, eventType, moreEventTypes);
    }

    public synchronized void registerSticky(Object subscriber, String methodName, Class<?> eventType, Class<?>... moreEventTypes) {
        register(subscriber, methodName, true, eventType, moreEventTypes);
    }

    private synchronized void register(Object subscriber, String methodName, boolean sticky, Class<?> eventType, Class<?>... moreEventTypes) {
        for (SubscriberMethod subscriberMethod : this.subscriberMethodFinder.findSubscriberMethods(subscriber.getClass(), methodName)) {
            if (eventType == subscriberMethod.eventType) {
                subscribe(subscriber, subscriberMethod, sticky);
            } else if (moreEventTypes != null) {
                int length = moreEventTypes.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    } else if (moreEventTypes[i] == subscriberMethod.eventType) {
                        subscribe(subscriber, subscriberMethod, sticky);
                        break;
                    } else {
                        i++;
                    }
                }
            } else {
                continue;
            }
        }
    }

    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod, boolean sticky) {
        Object stickyEvent;
        boolean z = true;
        this.subscribed = true;
        Class<?> eventType = subscriberMethod.eventType;
        CopyOnWriteArrayList<Subscription> subscriptions = this.subscriptionsByEventType.get(eventType);
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
            this.subscriptionsByEventType.put(eventType, subscriptions);
        } else {
            Iterator<Subscription> it = subscriptions.iterator();
            while (it.hasNext()) {
                if (it.next().equals(newSubscription)) {
                    throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered to event " + eventType);
                }
            }
        }
        subscriberMethod.method.setAccessible(true);
        subscriptions.add(newSubscription);
        List<Class<?>> subscribedEvents = this.typesBySubscriber.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<>();
            this.typesBySubscriber.put(subscriber, subscribedEvents);
        }
        subscribedEvents.add(eventType);
        if (sticky) {
            synchronized (this.stickyEvents) {
                stickyEvent = this.stickyEvents.get(eventType);
            }
            if (stickyEvent != null) {
                if (Looper.getMainLooper() != Looper.myLooper()) {
                    z = false;
                }
                postToSubscription(newSubscription, stickyEvent, z);
            }
        }
    }

    public synchronized void unregister(Object subscriber, Class<?>... eventTypes) {
        if (eventTypes.length == 0) {
            throw new IllegalArgumentException("Provide at least one event class");
        }
        List<Class<?>> subscribedClasses = this.typesBySubscriber.get(subscriber);
        if (subscribedClasses != null) {
            for (Class<?> eventType : eventTypes) {
                unubscribeByEventType(subscriber, eventType);
                subscribedClasses.remove(eventType);
            }
            if (subscribedClasses.isEmpty()) {
                this.typesBySubscriber.remove(subscriber);
            }
        } else {
            Log.w(TAG, "Subscriber to unregister was not registered before: " + subscriber.getClass());
        }
    }

    private void unubscribeByEventType(Object subscriber, Class<?> eventType) {
        List<Subscription> subscriptions = this.subscriptionsByEventType.get(eventType);
        if (subscriptions != null) {
            int size = subscriptions.size();
            int i = 0;
            while (i < size) {
                if (subscriptions.get(i).subscriber == subscriber) {
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
                i++;
            }
        }
    }

    public synchronized void unregister(Object subscriber) {
        List<Class<?>> subscribedTypes = this.typesBySubscriber.get(subscriber);
        if (subscribedTypes != null) {
            for (Class<?> eventType : subscribedTypes) {
                unubscribeByEventType(subscriber, eventType);
            }
            this.typesBySubscriber.remove(subscriber);
        } else {
            Log.w(TAG, "Subscriber to unregister was not registered before: " + subscriber.getClass());
        }
    }

    public void post(Object event) {
        boolean isMainThread;
        List<Object> eventQueue = this.currentThreadEventQueue.get();
        eventQueue.add(event);
        BooleanWrapper isPosting = this.currentThreadIsPosting.get();
        if (!isPosting.value) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                isMainThread = true;
            } else {
                isMainThread = false;
            }
            isPosting.value = true;
            while (!eventQueue.isEmpty()) {
                try {
                    postSingleEvent(eventQueue.remove(0), isMainThread);
                } finally {
                    isPosting.value = false;
                }
            }
        }
    }

    public void postSticky(Object event) {
        synchronized (this.stickyEvents) {
            this.stickyEvents.put(event.getClass(), event);
        }
        post(event);
    }

    public Object getStickyEvent(Class<?> eventType) {
        Object obj;
        synchronized (this.stickyEvents) {
            obj = this.stickyEvents.get(eventType);
        }
        return obj;
    }

    public Object removeStickyEvent(Class<?> eventType) {
        Object remove;
        synchronized (this.stickyEvents) {
            remove = this.stickyEvents.remove(eventType);
        }
        return remove;
    }

    public boolean removeStickyEvent(Object event) {
        synchronized (this.stickyEvents) {
            Class<?> cls = event.getClass();
            if (!event.equals(this.stickyEvents.get(cls))) {
                return false;
            }
            this.stickyEvents.remove(cls);
            return true;
        }
    }

    private void postSingleEvent(Object event, boolean isMainThread) throws Error {
        CopyOnWriteArrayList<Subscription> subscriptions;
        Class<?> cls = event.getClass();
        List<Class<?>> eventTypes = findEventTypes(cls);
        boolean subscriptionFound = false;
        int countTypes = eventTypes.size();
        for (int h = 0; h < countTypes; h++) {
            Class<?> clazz = eventTypes.get(h);
            synchronized (this) {
                subscriptions = this.subscriptionsByEventType.get(clazz);
            }
            if (subscriptions != null) {
                Iterator<Subscription> it = subscriptions.iterator();
                while (it.hasNext()) {
                    postToSubscription(it.next(), event, isMainThread);
                }
                subscriptionFound = true;
            }
        }
        if (!subscriptionFound) {
            Log.d(TAG, "No subscripers registered for event " + cls);
            if (cls != NoSubscriberEvent.class && cls != SubscriberExceptionEvent.class) {
                post(new NoSubscriberEvent(this, event));
            }
        }
    }

    private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
        switch ($SWITCH_TABLE$de$greenrobot$event$ThreadMode()[subscription.subscriberMethod.threadMode.ordinal()]) {
            case 1:
                invokeSubscriber(subscription, event);
                return;
            case 2:
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                    return;
                } else {
                    this.mainThreadPoster.enqueue(subscription, event);
                    return;
                }
            case 3:
                if (isMainThread) {
                    this.backgroundPoster.enqueue(subscription, event);
                    return;
                } else {
                    invokeSubscriber(subscription, event);
                    return;
                }
            case 4:
                this.asyncPoster.enqueue(subscription, event);
                return;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }

    private List<Class<?>> findEventTypes(Class<?> eventClass) {
        List<Class<?>> eventTypes;
        synchronized (eventTypesCache) {
            eventTypes = eventTypesCache.get(eventClass);
            if (eventTypes == null) {
                eventTypes = new ArrayList<>();
                for (Class<?> clazz = eventClass; clazz != null; clazz = clazz.getSuperclass()) {
                    eventTypes.add(clazz);
                    addInterfaces(eventTypes, clazz.getInterfaces());
                }
                eventTypesCache.put(eventClass, eventTypes);
            }
        }
        return eventTypes;
    }

    static void addInterfaces(List<Class<?>> eventTypes, Class<?>[] interfaces) {
        for (Class<?> interfaceClass : interfaces) {
            if (!eventTypes.contains(interfaceClass)) {
                eventTypes.add(interfaceClass);
                addInterfaces(eventTypes, interfaceClass.getInterfaces());
            }
        }
    }

    void invokeSubscriber(PendingPost pendingPost) {
        Object event = pendingPost.event;
        Subscription subscription = pendingPost.subscription;
        PendingPost.releasePendingPost(pendingPost);
        invokeSubscriber(subscription, event);
    }

    void invokeSubscriber(Subscription subscription, Object event) throws Error {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, new Object[]{event});
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (event instanceof SubscriberExceptionEvent) {
                Log.e(TAG, "SubscriberExceptionEvent subscriber " + subscription.subscriber.getClass() + " threw an exception", cause);
                SubscriberExceptionEvent exEvent = (SubscriberExceptionEvent) event;
                Log.e(TAG, "Initial event " + exEvent.causingEvent + " caused exception in " + exEvent.causingSubscriber, exEvent.throwable);
                return;
            }
            if (this.logSubscriberExceptions) {
                Log.e(TAG, "Could not dispatch event: " + event.getClass() + " to subscribing class " + subscription.subscriber.getClass(), cause);
            }
            post(new SubscriberExceptionEvent(this, cause, event, subscription.subscriber));
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Unexpected exception", e2);
        }
    }

    static final class BooleanWrapper {
        boolean value;

        BooleanWrapper() {
        }
    }
}
