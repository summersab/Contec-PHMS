package com.contec.phms.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;
    private ArrayList<Fragment> fragmentsList;

    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        if (this.fragmentsList != null) {
            FragmentTransaction ft = this.fm.beginTransaction();
            Iterator<Fragment> it = this.fragmentsList.iterator();
            while (it.hasNext()) {
                ft.remove(it.next());
            }
            ft.commit();
            this.fm.executePendingTransactions();
        }
        this.fragmentsList = fragments;
        notifyDataSetChanged();
    }

    public FragmentViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
        this.fm = fm;
    }

    public int getCount() {
        return this.fragmentsList.size();
    }

    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    public Fragment getItem(int arg0) {
        return this.fragmentsList.get(arg0);
    }

    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
