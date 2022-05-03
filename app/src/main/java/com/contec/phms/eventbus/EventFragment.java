package com.contec.phms.eventbus;

import androidx.fragment.app.Fragment;

public class EventFragment {
    private int mProgress;
    private int mWhichCommand;
    private Fragment mfragment;

    public void setmProgress(int mProgress2) {
        this.mProgress = mProgress2;
    }

    public int getmProgress() {
        return this.mProgress;
    }

    public int getmWhichCommand() {
        return this.mWhichCommand;
    }

    public void setmWhichCommand(int mWhichCommand2) {
        this.mWhichCommand = mWhichCommand2;
    }

    public EventFragment() {
    }

    public EventFragment(Fragment mfragment2) {
        this.mfragment = mfragment2;
    }

    public Fragment getMfragment() {
        return this.mfragment;
    }

    public void setMfragment(Fragment mfragment2) {
        this.mfragment = mfragment2;
    }
}
