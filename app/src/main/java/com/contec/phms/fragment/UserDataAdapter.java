package com.contec.phms.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.contec.phms.R;
import java.util.ArrayList;

/* compiled from: ActivityUserData */
class UserDataAdapter extends BaseAdapter {
    Holder _Holder;
    private Context mContext;
    private ArrayList<UserData> mUserDataList;

    public UserDataAdapter(Context pContext, ArrayList<UserData> pUserDataList) {
        this.mContext = pContext;
        this.mUserDataList = pUserDataList;
    }

    public int getCount() {
        return this.mUserDataList.size();
    }

    public Object getItem(int arg0) {
        return Integer.valueOf(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    /* compiled from: ActivityUserData */
    private class Holder {
        TextView muser_left_text;
        TextView muser_right_text;

        private Holder() {
        }

        /* synthetic */ Holder(UserDataAdapter userDataAdapter, Holder holder) {
            this();
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        this._Holder = new Holder(this, (Holder) null);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_data_userdataitem, (ViewGroup) null);
            this._Holder.muser_left_text = (TextView) convertView.findViewById(R.id.user_left_text);
            this._Holder.muser_right_text = (TextView) convertView.findViewById(R.id.user_right_text);
            convertView.setTag(this._Holder);
        } else {
            this._Holder = (Holder) convertView.getTag();
        }
        this._Holder.muser_left_text.setText(this.mUserDataList.get(position).getUser_left_text());
        this._Holder.muser_right_text.setText(this.mUserDataList.get(position).getUser_right_text());
        return convertView;
    }
}
