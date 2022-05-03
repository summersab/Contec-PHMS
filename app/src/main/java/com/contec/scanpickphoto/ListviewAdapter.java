package com.contec.scanpickphoto;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.contec.phms.R;
import com.contec.scanpickphoto.ImageLoader;
import java.util.List;

public class ListviewAdapter extends BaseAdapter {
    public static ViewGroup v = null;
    private Boolean b = true;
    private Context mContext;
    private List<ImageFloder> mDatas;
    private int selectedPosition = 1;

    public ListviewAdapter(Context mcontext, List<ImageFloder> mdatas) {
        this.mContext = mcontext;
        this.mDatas = mdatas;
    }

    public int getCount() {
        if (this.mDatas == null) {
            return 0;
        }
        return this.mDatas.size();
    }

    public Object getItem(int arg0) {
        return this.mDatas.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public View getView(int arg0, View arg1, ViewGroup arg2) {
        DIR viewHolder;
        if (arg1 == null) {
            arg1 = LayoutInflater.from(this.mContext).inflate(R.layout.pick_photo_item, (ViewGroup) null);
            viewHolder = new DIR();
            viewHolder.al = (ImageView) arg1.findViewById(R.id.abimageView);
            viewHolder.name = (TextView) arg1.findViewById(R.id.abname);
            viewHolder.count = (TextView) arg1.findViewById(R.id.abcount);
            viewHolder.choose = (ImageView) arg1.findViewById(R.id.choseimage);
            arg1.setTag(viewHolder);
        } else {
            viewHolder = (DIR) arg1.getTag();
        }
        viewHolder.name.setText(this.mDatas.get(arg0).getName());
        ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(this.mDatas.get(arg0).getFirstImagePath(), viewHolder.al);
        viewHolder.count.setText(new StringBuilder().append(this.mDatas.get(arg0).getCount()).toString());
        v = arg2;
        if (this.mDatas.get(arg0).getFlag().booleanValue()) {
            arg1.setBackgroundColor(Color.parseColor("#87CEFF"));
        } else {
            arg1.setBackgroundColor(0);
        }
        return arg1;
    }

    class DIR {
        ImageView al;
        ImageView choose;
        TextView count;
        TextView name;

        DIR() {
        }
    }
}
