package de.greenrobot.event;

final class Subscription {
    final Object subscriber;
    final SubscriberMethod subscriberMethod;

    Subscription(Object subscriber2, SubscriberMethod subscriberMethod2) {
        this.subscriber = subscriber2;
        this.subscriberMethod = subscriberMethod2;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Subscription)) {
            return false;
        }
        Subscription otherSubscription = (Subscription) other;
        if (this.subscriber != otherSubscription.subscriber || !this.subscriberMethod.equals(otherSubscription.subscriberMethod)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.subscriber.hashCode() + this.subscriberMethod.methodString.hashCode();
    }
}
