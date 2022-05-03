package de.greenrobot.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SubscriberMethodFinder {
    private static final Map<String, List<SubscriberMethod>> methodCache = new HashMap();
    private static final Map<Class<?>, Class<?>> skipMethodNameVerificationForClasses = new ConcurrentHashMap();

    SubscriberMethodFinder() {
    }

    List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass, String eventMethodName) {
        List<SubscriberMethod> subscriberMethods;
        ThreadMode threadMode = null;
        String key = String.valueOf(subscriberClass.getName()) + '.' + eventMethodName;
        synchronized (methodCache) {
            subscriberMethods = methodCache.get(key);
        }
        if (subscriberMethods != null) {
            return subscriberMethods;
        }
        List<SubscriberMethod> subscriberMethods2 = new ArrayList<>();
        HashSet<String> eventTypesFound = new HashSet<>();
        StringBuilder methodKeyBuilder = new StringBuilder();
        for (Class<?> clazz = subscriberClass; clazz != null; clazz = clazz.getSuperclass()) {
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }
            Method[] methods = clazz.getDeclaredMethods();
            int length = methods.length;
            for (int i = 0; i < length; i++) {
                Method method = methods[i];
                String methodName = method.getName();
                if (methodName.startsWith(eventMethodName)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1) {
                        String modifierString = methodName.substring(eventMethodName.length());
                        if (modifierString.length() == 0) {
                            threadMode = ThreadMode.PostThread;
                        } else if (modifierString.equals("MainThread")) {
                            threadMode = ThreadMode.MainThread;
                        } else if (modifierString.equals("BackgroundThread")) {
                            threadMode = ThreadMode.BackgroundThread;
                        } else if (modifierString.equals("Async")) {
                            threadMode = ThreadMode.Async;
                        } else if (!skipMethodNameVerificationForClasses.containsKey(clazz)) {
                            throw new EventBusException("Illegal onEvent method, check for typos: " + method);
                        }
                        Class<?> eventType = parameterTypes[0];
                        methodKeyBuilder.setLength(0);
                        methodKeyBuilder.append(methodName);
                        methodKeyBuilder.append('>').append(eventType.getName());
                        if (eventTypesFound.add(methodKeyBuilder.toString())) {
                            subscriberMethods2.add(new SubscriberMethod(method, threadMode, eventType));
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        if (subscriberMethods2.isEmpty()) {
            throw new EventBusException("Subscriber " + subscriberClass + " has no methods called " + eventMethodName);
        }
        synchronized (methodCache) {
            methodCache.put(key, subscriberMethods2);
        }
        return subscriberMethods2;
    }

    static void clearCaches() {
        methodCache.clear();
    }

    static void skipMethodNameVerificationFor(Class<?> clazz) {
        if (!methodCache.isEmpty()) {
            throw new IllegalStateException("This method must be called before registering anything");
        }
        skipMethodNameVerificationForClasses.put(clazz, clazz);
    }

    public static void clearSkipMethodNameVerifications() {
        skipMethodNameVerificationForClasses.clear();
    }
}
