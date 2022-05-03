package de.greenrobot.event.util;

import android.annotation.TargetApi;
import android.os.Bundle;

public abstract class ErrorDialogFragmentFactory<T> {
    protected final ErrorDialogConfig config;

    protected abstract T createErrorFragment(ThrowableFailureEvent throwableFailureEvent, Bundle bundle);

    protected ErrorDialogFragmentFactory(ErrorDialogConfig config2) {
        this.config = config2;
    }

    protected T prepareErrorFragment(ThrowableFailureEvent event, boolean finishAfterDialog, Bundle argumentsForErrorDialog) {
        Bundle bundle;
        if (event.isSuppressErrorUi()) {
            return null;
        }
        if (argumentsForErrorDialog != null) {
            bundle = (Bundle) argumentsForErrorDialog.clone();
        } else {
            bundle = new Bundle();
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_TITLE)) {
            bundle.putString(ErrorDialogManager.KEY_TITLE, getTitleFor(event, bundle));
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_MESSAGE)) {
            bundle.putString(ErrorDialogManager.KEY_MESSAGE, getMessageFor(event, bundle));
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_FINISH_AFTER_DIALOG)) {
            bundle.putBoolean(ErrorDialogManager.KEY_FINISH_AFTER_DIALOG, finishAfterDialog);
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_EVENT_TYPE_ON_CLOSE) && this.config.defaultEventTypeOnDialogClosed != null) {
            bundle.putSerializable(ErrorDialogManager.KEY_EVENT_TYPE_ON_CLOSE, this.config.defaultEventTypeOnDialogClosed);
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_ICON_ID) && this.config.defaultDialogIconId != 0) {
            bundle.putInt(ErrorDialogManager.KEY_ICON_ID, this.config.defaultDialogIconId);
        }
        return createErrorFragment(event, bundle);
    }

    protected String getTitleFor(ThrowableFailureEvent event, Bundle arguments) {
        return this.config.resources.getString(this.config.defaultTitleId);
    }

    protected String getMessageFor(ThrowableFailureEvent event, Bundle arguments) {
        return this.config.resources.getString(this.config.getMessageIdForThrowable(event.throwable));
    }

    public static class Support extends ErrorDialogFragmentFactory<ErrorDialogFragments.Support> {
        public Support(ErrorDialogConfig config) {
            super(config);
        }

        protected ErrorDialogFragments.Support createErrorFragment(ThrowableFailureEvent event, Bundle arguments) {
            ErrorDialogFragments.Support errorFragment = new ErrorDialogFragments.Support();
            errorFragment.setArguments(arguments);
            return errorFragment;
        }
    }

    @TargetApi(11)
    public static class Honeycomb extends ErrorDialogFragmentFactory<ErrorDialogFragments.Honeycomb> {
        public Honeycomb(ErrorDialogConfig config) {
            super(config);
        }

        public ErrorDialogFragments.Honeycomb createErrorFragment(ThrowableFailureEvent event, Bundle arguments) {
            ErrorDialogFragments.Honeycomb errorFragment = new ErrorDialogFragments.Honeycomb();
            errorFragment.setArguments(arguments);
            return errorFragment;
        }
    }
}
