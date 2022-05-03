package com.contec.phms.fragment;

import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import java.util.List;

public class ViewPagerAdaper extends PagerAdapter {
    List<View> views = null;

    public ViewPagerAdaper(List<View> mylistViews) {
        this.views = mylistViews;
    }

    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(this.views.get(arg1));
    }

    public void finishUpdate(View arg0) {
    }

    public int getCount() {
        return this.views.size();
    }

    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(this.views.get(arg1), 0);
        return this.views.get(arg1);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    public Parcelable saveState() {
        return null;
    }

    public void startUpdate(View arg0) {
    }
}
