package com.contec.phms.fragment;

import androidx.viewpager.widget.ViewPager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class ViewPagerListener implements ViewPager.OnPageChangeListener {
    int bmpW = 0;
    int currentIndex = 0;
    int offset = 0;
    int one = ((this.offset * 2) + this.bmpW);
    int three = (this.one * 3);
    int two = (this.one * 2);

    public void onPageScrollStateChanged(int arg0) {
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    public void onPageSelected(int arg0) {
        Animation anim = null;
        switch (arg0) {
            case 0:
                if (arg0 == 0) {
                    if (this.currentIndex != 1) {
                        if (this.currentIndex == 2) {
                            anim = new TranslateAnimation((float) this.two, 0.0f, 0.0f, 0.0f);
                            break;
                        }
                    } else {
                        anim = new TranslateAnimation((float) this.one, 0.0f, 0.0f, 0.0f);
                        break;
                    }
                }
                break;
            case 1:
                if (arg0 == 1) {
                    if (this.currentIndex != 0) {
                        if (this.currentIndex == 2) {
                            anim = new TranslateAnimation((float) this.two, (float) this.one, 0.0f, 0.0f);
                            break;
                        }
                    } else {
                        anim = new TranslateAnimation((float) this.offset, (float) this.one, 0.0f, 0.0f);
                        break;
                    }
                }
                break;
            case 2:
                if (arg0 == 2) {
                    if (this.currentIndex != 0) {
                        if (this.currentIndex != 1) {
                            if (this.currentIndex == 3) {
                                anim = new TranslateAnimation((float) this.three, (float) this.two, 0.0f, 0.0f);
                                break;
                            }
                        } else {
                            anim = new TranslateAnimation((float) this.one, (float) this.two, 0.0f, 0.0f);
                            break;
                        }
                    } else {
                        anim = new TranslateAnimation((float) this.offset, (float) this.two, 0.0f, 0.0f);
                        break;
                    }
                }
                break;
            case 3:
                if (arg0 == 3) {
                    if (this.currentIndex != 0) {
                        if (this.currentIndex == 2) {
                            anim = new TranslateAnimation((float) this.two, (float) this.three, 0.0f, 0.0f);
                            break;
                        }
                    } else {
                        anim = new TranslateAnimation((float) this.offset, (float) this.three, 0.0f, 0.0f);
                        break;
                    }
                }
                break;
        }
        this.currentIndex = arg0;
        anim.setFillAfter(true);
        anim.setDuration(200);
    }
}
