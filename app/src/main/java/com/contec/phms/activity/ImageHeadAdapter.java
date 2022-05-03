package com.contec.phms.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.contec.circleimage.widget.CircularImage;
import com.contec.phms.R;
import com.contec.phms.fragment.ImageHead;
import java.util.ArrayList;

/* compiled from: ActivityChooseHead */
class ImageHeadAdapter extends BaseAdapter {
    private final int BEFORE_OK = 1;
    Holder _Holder;
    ImageView _Selected;
    private Context mContext;
    private ArrayList<ImageHead> mImageHeadList;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (ActivityChooseHead.isSelectPic != msg.arg1) {
                        ImageHeadAdapter.this._Selected.setVisibility(View.GONE);
                        return;
                    } else if (ImageHeadAdapter.this._Selected != null) {
                        ImageHeadAdapter.this._Selected.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        ImageHeadAdapter.this._Selected.setVisibility(View.GONE);
                        return;
                    }
                default:
                    return;
            }
        }
    };

    ImageHeadAdapter(Context pContext, ArrayList<ImageHead> ImageHeadList) {
        this.mContext = pContext;
        this.mImageHeadList = ImageHeadList;
    }

    public int getCount() {
        return this.mImageHeadList.size();
    }

    public Object getItem(int arg0) {
        return Integer.valueOf(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    /* compiled from: ActivityChooseHead */
    private class Holder {
        CircularImage mImageHead;
        ImageView mSelected;

        private Holder() {
        }

        /* synthetic */ Holder(ImageHeadAdapter imageHeadAdapter, Holder holder) {
            this();
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        this._Holder = new Holder(this, (Holder) null);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_chooseheaditem, (ViewGroup) null);
            this._Holder.mImageHead = (CircularImage) convertView.findViewById(R.id.imagehead);
            this._Holder.mSelected = (ImageView) convertView.findViewById(R.id.isselected);
            convertView.setTag(this._Holder);
        } else {
            this._Holder = (Holder) convertView.getTag();
        }
        this._Holder.mImageHead.setImageResource(this.mImageHeadList.get(position).getImage_head());
        if (ActivityChooseHead.isSelectPicSec == position && ActivityChooseHead.isSelectPic == position) {
            convertView.startAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.choose_head_scale));
            this._Selected = this._Holder.mSelected;
            Message message = new Message();
            message.what = 1;
            message.arg1 = position;
            this.myHandler.sendMessageDelayed(message, 400);
        } else if (ActivityChooseHead.isSelectPicSec == position || ActivityChooseHead.isSelectPic != position) {
            this._Holder.mSelected.setVisibility(View.GONE);
        } else {
            this._Selected = this._Holder.mSelected;
            Message message2 = new Message();
            message2.what = 1;
            message2.arg1 = position;
            this.myHandler.sendMessage(message2);
        }
        return convertView;
    }
}
