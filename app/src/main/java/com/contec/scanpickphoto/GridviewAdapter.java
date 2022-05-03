package com.contec.scanpickphoto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.contec.phms.R;
import com.contec.scanpickphoto.ImageLoader;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class GridviewAdapter extends BaseAdapter {
    private String dirPath;
    private Context mContext;
    private List<String> mDatas;
    private int mItemLayoutId;

    public GridviewAdapter(Context context, List<String> mDatas2, int itemLayoutId, String dirPath2) {
        this.mContext = context;
        this.mDatas = mDatas2;
        this.mItemLayoutId = itemLayoutId;
        this.dirPath = dirPath2;
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

    public View getView(int arg0, View arg1, ViewGroup arg2) {
        u viewHolder;
        if (arg1 == null) {
            arg1 = LayoutInflater.from(this.mContext).inflate(this.mItemLayoutId, (ViewGroup) null);
            viewHolder = new u();
            viewHolder.i = (ImageView) arg1.findViewById(R.id.id_item_image);
            arg1.setTag(viewHolder);
        } else {
            viewHolder = (u) arg1.getTag();
        }
        ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(String.valueOf(this.dirPath) + CookieSpec.PATH_DELIM + this.mDatas.get(arg0), viewHolder.i);
        return arg1;
    }

    class u {

        ImageView i;

        u() {
        }
    }
}
