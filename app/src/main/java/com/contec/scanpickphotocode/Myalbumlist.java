package com.contec.scanpickphotocode;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.contec.phms.R;
import com.contec.scanpickphoto.ImageFloder;
import com.contec.scanpickphoto.ListviewAdapter;
import java.util.List;

public class Myalbumlist extends PopupWindow {
    private static final String TAG = "CustomMenu";
    static ListView alist;
    static ListviewAdapter listadapter;
    private static PopupWindow pop = null;
    private final Activity activity;
    ImageButton left;
    List<ImageFloder> mImageFloders;
    View view;

    public Myalbumlist(Activity activity2) {
        this.activity = activity2;
    }

    public PopupWindow getMenu(Context context, int wwidth, List<ImageFloder> mImageFloders2, View.OnClickListener clickListener, AdapterView.OnItemClickListener ClickListener) {
        this.mImageFloders = mImageFloders2;
        this.view = this.activity.getLayoutInflater().inflate(R.layout.dialod_album, (ViewGroup) null);
        pop = new PopupWindow(this.view, (wwidth / 20) * 9, -1);
        pop.setBackgroundDrawable(this.activity.getResources().getDrawable(R.drawable.pop));
        pop.setAnimationStyle(R.style.AnimationFade);
        pop.setFocusable(true);
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        alist = (ListView) this.view.findViewById(R.id.albumlist);
        this.left = (ImageButton) this.view.findViewById(R.id.leftimageBu);
        alist.setVerticalScrollBarEnabled(false);
        this.left.setOnClickListener(clickListener);
        listadapter = new ListviewAdapter(this.activity, mImageFloders2);
        alist.setAdapter(listadapter);
        alist.setOnItemClickListener(ClickListener);
        return pop;
    }
}
